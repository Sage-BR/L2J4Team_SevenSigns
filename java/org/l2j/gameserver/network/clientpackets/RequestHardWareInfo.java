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
package org.l2j.gameserver.network.clientpackets;

import org.l2j.commons.network.ReadablePacket;
import org.l2j.gameserver.model.holders.ClientHardwareInfoHolder;
import org.l2j.gameserver.network.GameClient;

/**
 * @author Mobius
 */
public class RequestHardWareInfo implements ClientPacket
{
	private String _macAddress;
	private int _windowsPlatformId;
	private int _windowsMajorVersion;
	private int _windowsMinorVersion;
	private int _windowsBuildNumber;
	private int _directxVersion;
	private int _directxRevision;
	private String _cpuName;
	private int _cpuSpeed;
	private int _cpuCoreCount;
	private int _vgaCount;
	private int _vgaPcxSpeed;
	private int _physMemorySlot1;
	private int _physMemorySlot2;
	private int _physMemorySlot3;
	private int _videoMemory;
	private int _vgaVersion;
	private String _vgaName;
	private String _vgaDriverVersion;
	
	@Override
	public void read(ReadablePacket packet)
	{
		_macAddress = packet.readString();
		_windowsPlatformId = packet.readInt();
		_windowsMajorVersion = packet.readInt();
		_windowsMinorVersion = packet.readInt();
		_windowsBuildNumber = packet.readInt();
		_directxVersion = packet.readInt();
		_directxRevision = packet.readInt();
		packet.readBytes(16);
		_cpuName = packet.readString();
		_cpuSpeed = packet.readInt();
		_cpuCoreCount = packet.readByte();
		packet.readInt();
		_vgaCount = packet.readInt();
		_vgaPcxSpeed = packet.readInt();
		_physMemorySlot1 = packet.readInt();
		_physMemorySlot2 = packet.readInt();
		_physMemorySlot3 = packet.readInt();
		packet.readByte();
		_videoMemory = packet.readInt();
		packet.readInt();
		_vgaVersion = packet.readShort();
		_vgaName = packet.readString();
		_vgaDriverVersion = packet.readString();
	}
	
	@Override
	public void run(GameClient client)
	{
		client.setHardwareInfo(new ClientHardwareInfoHolder(_macAddress, _windowsPlatformId, _windowsMajorVersion, _windowsMinorVersion, _windowsBuildNumber, _directxVersion, _directxRevision, _cpuName, _cpuSpeed, _cpuCoreCount, _vgaCount, _vgaPcxSpeed, _physMemorySlot1, _physMemorySlot2, _physMemorySlot3, _videoMemory, _vgaVersion, _vgaName, _vgaDriverVersion));
	}
}
