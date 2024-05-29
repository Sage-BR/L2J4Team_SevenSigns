package org.l2j.commons.network;

/**
 * @author Pantelis Andrianakis
 * @since October 4th 2022
 */
public class NetConfig
{
	private int _readPoolSize = 100;
	private int _sendPoolSize = 25;
	private int _executePoolSize = 50;
	private int _packetQueueLimit = 80;
	private boolean _packetFloodDisconnect = false;
	private boolean _packetFloodDrop = false;
	private boolean _packetFloodLogged = true;
	private boolean _failedDecryptionLogged = true;
	private boolean _tcpNoDelay = true;
	
	/**
	 * @return the NetClient pool size for reading client packets.
	 */
	public int getReadPoolSize()
	{
		return _readPoolSize;
	}
	
	/**
	 * Sets the NetClient pool size for reading client packets.
	 * @param clientPoolSize
	 */
	public void setReadPoolSize(int clientPoolSize)
	{
		_readPoolSize = clientPoolSize;
	}
	
	/**
	 * @return the NetClient pool size for sending server packets.
	 */
	public int getSendPoolSize()
	{
		return _sendPoolSize;
	}
	
	/**
	 * Sets the NetClient pool size for sending server packets.
	 * @param clientPoolSize
	 */
	public void setSendPoolSize(int clientPoolSize)
	{
		_sendPoolSize = clientPoolSize;
	}
	
	/**
	 * @return the NetClient pool size for executing client packets.
	 */
	public int getExecutePoolSize()
	{
		return _executePoolSize;
	}
	
	/**
	 * Sets the NetClient pool size for executing client packets.
	 * @param executePoolSize
	 */
	public void setExecutePoolSize(int executePoolSize)
	{
		_executePoolSize = executePoolSize;
	}
	
	/**
	 * @return the packet queue limit of receivable packets.
	 */
	public int getPacketQueueLimit()
	{
		return _packetQueueLimit;
	}
	
	/**
	 * Sets the packet queue limit of receivable packets.
	 * @param packetQueueLimit
	 */
	public void setPacketQueueLimit(int packetQueueLimit)
	{
		_packetQueueLimit = packetQueueLimit;
	}
	
	/**
	 * @return if disconnect when packets that exceed the packet queue limit.
	 */
	public boolean isPacketFloodDisconnect()
	{
		return _packetFloodDisconnect;
	}
	
	/**
	 * Sets to disconnect when the packet queue limit is exceeded.
	 * @param packetFloodDisconnect
	 */
	public void setPacketFloodDisconnect(boolean packetFloodDisconnect)
	{
		_packetFloodDisconnect = packetFloodDisconnect;
	}
	
	/**
	 * @return if packets that exceed the packet queue limit are dropped.
	 */
	public boolean isPacketFloodDrop()
	{
		return _packetFloodDrop;
	}
	
	/**
	 * Sets if packets that exceed the packet queue limit are dropped.
	 * @param packetQueueDrop
	 */
	public void setPacketFloodDrop(boolean packetQueueDrop)
	{
		_packetFloodDrop = packetQueueDrop;
	}
	
	/**
	 * @return if dropped packets are logged.
	 */
	public boolean isPacketFloodLogged()
	{
		return _packetFloodLogged;
	}
	
	/**
	 * Sets if dropped packets are logged.
	 * @param packetFloodLogged
	 */
	public void setPacketFloodLogged(boolean packetFloodLogged)
	{
		_packetFloodLogged = packetFloodLogged;
	}
	
	/**
	 * @return if failed decryption is logged.
	 */
	public boolean isFailedDecryptionLogged()
	{
		return _failedDecryptionLogged;
	}
	
	/**
	 * Sets if failed decryption is logged.
	 * @param failedDecryptionLogged
	 */
	public void setFailedDecryptionLogged(boolean failedDecryptionLogged)
	{
		_failedDecryptionLogged = failedDecryptionLogged;
	}
	
	/**
	 * @return if TCP_NODELAY (Nagle's Algorithm) is used.
	 */
	public boolean isTcpNoDelay()
	{
		return _tcpNoDelay;
	}
	
	/**
	 * Sets if TCP_NODELAY (Nagle's Algorithm) is used.
	 * @param tcpNoDelay
	 */
	public void setTcpNoDelay(boolean tcpNoDelay)
	{
		_tcpNoDelay = tcpNoDelay;
	}
}
