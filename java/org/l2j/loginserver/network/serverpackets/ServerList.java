/*
 * This file is part of the L2J 4Team project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.l2j.loginserver.network.serverpackets;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.l2j.commons.network.WritablePacket;
import org.l2j.loginserver.GameServerTable;
import org.l2j.loginserver.GameServerTable.GameServerInfo;
import org.l2j.loginserver.network.LoginClient;
import org.l2j.loginserver.network.LoginServerPackets;
import org.l2j.loginserver.network.gameserverpackets.ServerStatus;

/**
 * ServerList
 * 
 * <pre>
 * Format: cc [cddcchhcdc]
 * 
 * c: server list size (number of servers)
 * c: ?
 * [ (repeat for each servers)
 * c: server id (ignored by client?)
 * d: server ip
 * d: server port
 * c: age limit (used by client?)
 * c: pvp or not (used by client?)
 * h: current number of players
 * h: max number of players
 * c: 0 if server is down
 * d: 2nd bit: clock
 *    3rd bit: won't display server name
 *    4th bit: test server (used by client?)
 * c: 0 if you don't want to display brackets in front of sever name
 * ]
 * </pre>
 * 
 * Server will be considered as Good when the number of online players<br>
 * is less than half the maximum. as Normal between half and 4/5<br>
 * and Full when there's more than 4/5 of the maximum number of players.
 */
public class ServerList extends WritablePacket
{
	protected static final Logger LOGGER = Logger.getLogger(ServerList.class.getName());
	
	private final List<ServerData> _servers;
	private final int _lastServer;
	private Map<Integer, Integer> _charsOnServers;
	
	class ServerData
	{
		protected byte[] _ip;
		protected int _port;
		protected int _ageLimit;
		protected boolean _pvp;
		protected int _currentPlayers;
		protected int _maxPlayers;
		protected boolean _brackets;
		protected boolean _clock;
		protected int _status;
		protected int _serverId;
		protected int _serverType;
		
		ServerData(LoginClient client, GameServerInfo gsi)
		{
			try
			{
				_ip = InetAddress.getByName(gsi.getServerAddress(InetAddress.getByName(client.getIp()))).getAddress();
			}
			catch (UnknownHostException e)
			{
				LOGGER.warning(getClass().getSimpleName() + ": " + e.getMessage());
				_ip = new byte[4];
				_ip[0] = 127;
				_ip[1] = 0;
				_ip[2] = 0;
				_ip[3] = 1;
			}
			
			_port = gsi.getPort();
			_pvp = gsi.isPvp();
			_serverType = gsi.getServerType();
			_currentPlayers = gsi.getCurrentPlayerCount();
			_maxPlayers = gsi.getMaxPlayers();
			_ageLimit = 0;
			_brackets = gsi.isShowingBrackets();
			// If server GM-only - show status only to GMs
			_status = (client.getAccessLevel() < 0) || ((gsi.getStatus() == ServerStatus.STATUS_GM_ONLY) && (client.getAccessLevel() <= 0)) ? ServerStatus.STATUS_DOWN : gsi.getStatus();
			_serverId = gsi.getId();
		}
	}
	
	public ServerList(LoginClient client)
	{
		_servers = new ArrayList<>(GameServerTable.getInstance().getRegisteredGameServers().size());
		_lastServer = client.getLastServer();
		for (GameServerInfo gsi : GameServerTable.getInstance().getRegisteredGameServers().values())
		{
			_servers.add(new ServerData(client, gsi));
		}
		
		// Wait 500ms to reply with character list.
		int i = 0;
		while ((_charsOnServers == null) && (i++ < 10))
		{
			try
			{
				Thread.sleep(50);
			}
			catch (InterruptedException ignored)
			{
			}
			_charsOnServers = client.getCharsOnServ();
		}
	}
	
	@Override
	public void write()
	{
		LoginServerPackets.SERVER_LIST.writeId(this);
		writeByte(_servers.size());
		writeByte(_lastServer);
		for (ServerData server : _servers)
		{
			writeByte(server._serverId); // server id
			
			writeByte(server._ip[0] & 0xff);
			writeByte(server._ip[1] & 0xff);
			writeByte(server._ip[2] & 0xff);
			writeByte(server._ip[3] & 0xff);
			
			writeInt(server._port);
			writeByte(server._ageLimit); // Age Limit 0, 15, 18
			writeByte(server._pvp ? 0x01 : 0x00);
			writeShort(server._currentPlayers);
			writeShort(server._maxPlayers);
			writeByte(server._status == ServerStatus.STATUS_DOWN ? 0x00 : 0x01);
			writeInt(server._serverType); // 1: Normal, 2: Relax, 4: Public Test, 8: No Label, 16: Character Creation Restricted, 32: Event, 64: Free
			writeByte(server._brackets ? 0x01 : 0x00);
		}
		writeShort(0xA4); // unknown
		if (_charsOnServers != null)
		{
			for (ServerData server : _servers)
			{
				writeByte(server._serverId);
				writeByte(_charsOnServers.getOrDefault(server._serverId, 0));
			}
		}
	}
}
