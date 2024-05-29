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
package org.l2j.gameserver.network;

import org.l2j.commons.network.EncryptionInterface;

/**
 * @author KenM
 */
public class Encryption implements EncryptionInterface
{
	private final byte[] _inKey = new byte[16];
	private final byte[] _outKey = new byte[16];
	private boolean _isEnabled;
	
	public void setKey(byte[] key)
	{
		System.arraycopy(key, 0, _inKey, 0, 16);
		System.arraycopy(key, 0, _outKey, 0, 16);
	}
	
	@Override
	public void encrypt(byte[] data, int offset, int size)
	{
		if (!_isEnabled)
		{
			_isEnabled = true;
			return;
		}
		
		int a = 0;
		for (int i = 0; i < size; i++)
		{
			final int b = data[offset + i] & 0xff;
			a = b ^ _outKey[i & 15] ^ a;
			data[offset + i] = (byte) a;
		}
		
		// Shift key.
		int old = _outKey[8] & 0xff;
		old |= (_outKey[9] << 8) & 0xff00;
		old |= (_outKey[10] << 16) & 0xff0000;
		old |= (_outKey[11] << 24) & 0xff000000;
		old += size;
		_outKey[8] = (byte) (old & 0xff);
		_outKey[9] = (byte) ((old >> 8) & 0xff);
		_outKey[10] = (byte) ((old >> 16) & 0xff);
		_outKey[11] = (byte) ((old >> 24) & 0xff);
	}
	
	@Override
	public void decrypt(byte[] data, int offset, int size)
	{
		if (!_isEnabled)
		{
			return;
		}
		
		int a = 0;
		for (int i = 0; i < size; i++)
		{
			final int b = data[offset + i] & 0xff;
			data[offset + i] = (byte) (b ^ _inKey[i & 15] ^ a);
			a = b;
		}
		
		// Shift key.
		int old = _inKey[8] & 0xff;
		old |= (_inKey[9] << 8) & 0xff00;
		old |= (_inKey[10] << 16) & 0xff0000;
		old |= (_inKey[11] << 24) & 0xff000000;
		old += size;
		_inKey[8] = (byte) (old & 0xff);
		_inKey[9] = (byte) ((old >> 8) & 0xff);
		_inKey[10] = (byte) ((old >> 16) & 0xff);
		_inKey[11] = (byte) ((old >> 24) & 0xff);
	}
}
