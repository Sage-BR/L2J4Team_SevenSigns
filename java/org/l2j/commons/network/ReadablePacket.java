package org.l2j.commons.network;

import java.nio.charset.StandardCharsets;

/**
 * @author Pantelis Andrianakis
 * @since October 29th 2020
 */
public class ReadablePacket
{
	private final byte[] _bytes;
	private int _position = 0;
	
	public ReadablePacket(byte[] bytes)
	{
		_bytes = bytes;
	}
	
	/**
	 * Reads <b>boolean</b> from the packet data.<br>
	 * 8bit integer (00) or (01)
	 * @return
	 */
	public boolean readBoolean()
	{
		return readByte() != 0;
	}
	
	/**
	 * Reads <b>String</b> from the packet data.
	 * @return
	 */
	public String readString()
	{
		final StringBuilder result = new StringBuilder();
		try
		{
			int charId;
			while ((charId = readShort()) != 0)
			{
				result.append((char) charId);
			}
		}
		catch (Exception ignored)
		{
		}
		return result.toString();
	}
	
	/**
	 * Reads <b>String</b> with fixed size specified as (short size, char[size]) from the packet data.
	 * @return
	 */
	public String readSizedString()
	{
		String result = "";
		try
		{
			result = new String(readBytes(readShort() * 2), StandardCharsets.UTF_16LE);
		}
		catch (Exception ignored)
		{
		}
		return result;
	}
	
	/**
	 * Reads <b>byte[]</b> from the packet data.<br>
	 * 8bit integer array (00...)
	 * @param length of the array.
	 * @return
	 */
	public byte[] readBytes(int length)
	{
		final byte[] result = new byte[length];
		for (int i = 0; i < length; i++)
		{
			result[i] = _bytes[_position++];
		}
		return result;
	}
	
	/**
	 * Reads <b>byte[]</b> from the packet data.<br>
	 * 8bit integer array (00...)
	 * @param array used to store data.
	 * @return
	 */
	public byte[] readBytes(byte[] array)
	{
		for (int i = 0; i < array.length; i++)
		{
			array[i] = _bytes[_position++];
		}
		return array;
	}
	
	/**
	 * Reads <b>byte</b> from the packet data.<br>
	 * 8bit integer (00)
	 * @return
	 */
	public int readByte()
	{
		return _bytes[_position++] & 0xff;
	}
	
	/**
	 * Reads <b>short</b> from the packet data.<br>
	 * 16bit integer (00 00)
	 * @return
	 */
	public int readShort()
	{
		return (_bytes[_position++] & 0xff) //
			| ((_bytes[_position++] & 0xff) << 8);
	}
	
	/**
	 * Reads <b>int</b> from the packet data.<br>
	 * 32bit integer (00 00 00 00)
	 * @return
	 */
	public int readInt()
	{
		return (_bytes[_position++] & 0xff) //
			| ((_bytes[_position++] & 0xff) << 8) //
			| ((_bytes[_position++] & 0xff) << 16) //
			| ((_bytes[_position++] & 0xff) << 24);
	}
	
	/**
	 * Reads <b>long</b> from the packet data.<br>
	 * 64bit integer (00 00 00 00 00 00 00 00)
	 * @return
	 */
	public long readLong()
	{
		return (_bytes[_position++] & 0xff) //
			| ((_bytes[_position++] & 0xffL) << 8) //
			| ((_bytes[_position++] & 0xffL) << 16) //
			| ((_bytes[_position++] & 0xffL) << 24) //
			| ((_bytes[_position++] & 0xffL) << 32) //
			| ((_bytes[_position++] & 0xffL) << 40) //
			| ((_bytes[_position++] & 0xffL) << 48) //
			| ((_bytes[_position++] & 0xffL) << 56);
	}
	
	/**
	 * Reads <b>float</b> from the packet data.<br>
	 * 32bit single precision float (00 00 00 00)
	 * @return
	 */
	public float readFloat()
	{
		return Float.intBitsToFloat(readInt());
	}
	
	/**
	 * Reads <b>double</b> from the packet data.<br>
	 * 64bit double precision float (00 00 00 00 00 00 00 00)
	 * @return
	 */
	public double readDouble()
	{
		return Double.longBitsToDouble(readLong());
	}
	
	/**
	 * @return the number of unread bytes.
	 */
	public int getRemainingLength()
	{
		return _bytes.length - _position;
	}
	
	/**
	 * @return the byte size.
	 */
	public int getLength()
	{
		return _bytes.length;
	}
}
