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
package org.l2j.gameserver.network.loginserverpackets.game;

import java.security.interfaces.RSAPublicKey;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;

import org.l2j.commons.network.WritablePacket;

/**
 * @author -Wooden-
 */
public class BlowFishKey extends WritablePacket
{
	private static final Logger LOGGER = Logger.getLogger(BlowFishKey.class.getName());
	
	/**
	 * @param blowfishKey
	 * @param publicKey
	 */
	public BlowFishKey(byte[] blowfishKey, RSAPublicKey publicKey)
	{
		writeByte(0x00);
		try
		{
			final Cipher rsaCipher = Cipher.getInstance("RSA/ECB/nopadding");
			rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
			final byte[] encrypted = rsaCipher.doFinal(blowfishKey);
			writeInt(encrypted.length);
			writeBytes(encrypted);
		}
		catch (Exception e)
		{
			LOGGER.log(Level.SEVERE, "Error While encrypting blowfish key for transmision (Crypt error): " + e.getMessage(), e);
		}
	}
}
