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
package org.l2j.loginserver.network;

import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.l2j.commons.network.EncryptionInterface;
import org.l2j.commons.network.NetClient;
import org.l2j.commons.network.WritablePacket;
import org.l2j.commons.util.Rnd;
import org.l2j.loginserver.LoginController;
import org.l2j.loginserver.SessionKey;
import org.l2j.loginserver.enums.LoginFailReason;
import org.l2j.loginserver.enums.PlayFailReason;
import org.l2j.loginserver.network.serverpackets.Init;
import org.l2j.loginserver.network.serverpackets.LoginFail;
import org.l2j.loginserver.network.serverpackets.PlayFail;

/**
 * Represents a client connected into the LoginServer
 * @author KenM
 */
public class LoginClient extends NetClient
{
	private ScrambledKeyPair _scrambledPair;
	private SecretKey _blowfishKey;
	
	private String _account;
	private int _accessLevel;
	private int _lastServer;
	private SessionKey _sessionKey;
	private int _sessionId;
	private boolean _joinedGS;
	private Map<Integer, Integer> _charsOnServers;
	private Map<Integer, long[]> _charsToDelete;
	private ConnectionState _connectionState = ConnectionState.CONNECTED;
	private long _connectionStartTime;
	private final LoginEncryption _encryption = new LoginEncryption();
	
	@Override
	public void onConnection()
	{
		_blowfishKey = LoginController.getInstance().generateBlowfishKey();
		_encryption.setKey(_blowfishKey.getEncoded());
		_scrambledPair = LoginController.getInstance().getScrambledRSAKeyPair();
		_sessionId = Rnd.nextInt();
		_connectionStartTime = System.currentTimeMillis();
		sendPacket(new Init(_scrambledPair.getScrambledModulus(), _blowfishKey.getEncoded(), _sessionId));
		
		if (LoginController.getInstance().isBannedAddress(getIp()))
		{
			sendPacket(new LoginFail(LoginFailReason.REASON_NOT_AUTHED));
			disconnect();
		}
	}
	
	@Override
	public void onDisconnection()
	{
		if (!_joinedGS || ((_connectionStartTime + LoginController.LOGIN_TIMEOUT) < System.currentTimeMillis()))
		{
			LoginController.getInstance().removeAuthedLoginClient(getAccount());
		}
	}
	
	public String getAccount()
	{
		return _account;
	}
	
	public void setAccount(String account)
	{
		_account = account;
	}
	
	public void setAccessLevel(int accessLevel)
	{
		_accessLevel = accessLevel;
	}
	
	public int getAccessLevel()
	{
		return _accessLevel;
	}
	
	public void setLastServer(int lastServer)
	{
		_lastServer = lastServer;
	}
	
	public int getLastServer()
	{
		return _lastServer;
	}
	
	public int getSessionId()
	{
		return _sessionId;
	}
	
	public ScrambledKeyPair getScrambledKeyPair()
	{
		return _scrambledPair;
	}
	
	public boolean hasJoinedGS()
	{
		return _joinedGS;
	}
	
	public void setJoinedGS(boolean value)
	{
		_joinedGS = value;
	}
	
	public void setSessionKey(SessionKey sessionKey)
	{
		_sessionKey = sessionKey;
	}
	
	public SessionKey getSessionKey()
	{
		return _sessionKey;
	}
	
	public long getConnectionStartTime()
	{
		return _connectionStartTime;
	}
	
	@Override
	public void sendPacket(WritablePacket packet)
	{
		final Socket socket = getSocket();
		if ((socket != null) && socket.isConnected())
		{
			final byte[] sendableBytes = packet.getSendableBytes();
			if (sendableBytes == null)
			{
				return;
			}
			
			try
			{
				final OutputStream outputStream = getOutputStream();
				synchronized (this)
				{
					outputStream.write(sendableBytes);
					outputStream.flush();
				}
			}
			catch (Exception ignored)
			{
			}
		}
	}
	
	public void close(LoginFailReason reason)
	{
		close(new LoginFail(reason));
	}
	
	public void close(PlayFailReason reason)
	{
		close(new PlayFail(reason));
	}
	
	public void close(WritablePacket packet)
	{
		sendPacket(packet);
		closeNow();
	}
	
	public void closeNow()
	{
		disconnect();
	}
	
	public void setCharsOnServ(int servId, int chars)
	{
		if (_charsOnServers == null)
		{
			_charsOnServers = new HashMap<>();
		}
		_charsOnServers.put(servId, chars);
	}
	
	public Map<Integer, Integer> getCharsOnServ()
	{
		return _charsOnServers;
	}
	
	public void serCharsWaitingDelOnServ(int servId, long[] charsToDel)
	{
		if (_charsToDelete == null)
		{
			_charsToDelete = new HashMap<>();
		}
		_charsToDelete.put(servId, charsToDel);
	}
	
	public Map<Integer, long[]> getCharsWaitingDelOnServ()
	{
		return _charsToDelete;
	}
	
	public ConnectionState getConnectionState()
	{
		return _connectionState;
	}
	
	public void setConnectionState(ConnectionState connectionState)
	{
		_connectionState = connectionState;
	}
	
	@Override
	public EncryptionInterface getEncryption()
	{
		return _encryption;
	}
	
	@Override
	public String toString()
	{
		final String ip = getIp();
		final StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append(" [");
		if (_account != null)
		{
			sb.append("Account: ");
			sb.append(_account);
		}
		if (ip != null)
		{
			if (_account != null)
			{
				sb.append(" - ");
			}
			sb.append("IP: ");
			sb.append(ip);
		}
		sb.append("]");
		return sb.toString();
	}
}
