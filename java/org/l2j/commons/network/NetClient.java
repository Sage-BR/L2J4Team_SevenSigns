package org.l2j.commons.network;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

/**
 * @author Pantelis Andrianakis
 * @since September 7th 2020
 */
public class NetClient
{
	protected static final Logger LOGGER = Logger.getLogger(NetClient.class.getName());
	
	private final Queue<byte[]> _receivedData = new ConcurrentLinkedQueue<>();
	private final Queue<WritablePacket> _sendPacketQueue = new ConcurrentLinkedQueue<>();
	private final AtomicBoolean _isSending = new AtomicBoolean();
	private final AtomicBoolean _isRunning = new AtomicBoolean();
	
	private String _ip;
	private Socket _socket;
	private InputStream _inputStream;
	private OutputStream _outputStream;
	private NetConfig _netConfig;
	
	private byte[] _pendingData;
	private int _pendingPacketSize;
	
	/**
	 * Initialize the client.
	 * @param socket
	 * @param netConfig
	 */
	public void init(Socket socket, NetConfig netConfig)
	{
		_socket = socket;
		_netConfig = netConfig;
		_ip = socket.getInetAddress().toString().substring(1); // Trim out /127.0.0.1
		
		try
		{
			_inputStream = _socket.getInputStream();
			_outputStream = _socket.getOutputStream();
		}
		catch (Exception ignored)
		{
		}
		
		// Client is ready for communication.
		onConnection();
	}
	
	/**
	 * Called when client is connected.
	 */
	public void onConnection()
	{
	}
	
	/**
	 * Called when client is disconnected.
	 */
	public void onDisconnection()
	{
	}
	
	/**
	 * Disconnect the client.
	 */
	public void disconnect()
	{
		if (_inputStream != null)
		{
			try
			{
				_inputStream.close();
				_inputStream = null;
			}
			catch (Exception ignored)
			{
			}
		}
		
		if (_outputStream != null)
		{
			try
			{
				_outputStream.close();
				_outputStream = null;
			}
			catch (Exception ignored)
			{
			}
		}
		
		if (_socket != null)
		{
			try
			{
				_socket.close();
				_socket = null;
			}
			catch (Exception ignored)
			{
			}
		}
		
		_receivedData.clear();
		_sendPacketQueue.clear();
		
		if (_pendingData != null)
		{
			_pendingData = null;
		}
		
		// Client is disconnected.
		onDisconnection();
	}
	
	/**
	 * Add packet data to the queue.
	 * @param data
	 */
	public void addReceivedData(byte[] data)
	{
		// Check packet flooding.
		final int size = _receivedData.size();
		if (size >= _netConfig.getPacketQueueLimit())
		{
			if (_netConfig.isPacketFloodDisconnect())
			{
				disconnect();
				return;
			}
			
			if (_netConfig.isPacketFloodDrop())
			{
				if (_netConfig.isPacketFloodLogged() && ((size % _netConfig.getPacketQueueLimit()) == 0))
				{
					final StringBuilder sb = new StringBuilder();
					sb.append(this);
					sb.append(" packet queue size(");
					sb.append(size);
					sb.append(") exceeded limit(");
					sb.append(_netConfig.getPacketQueueLimit());
					sb.append(").");
					LOGGER.warning(sb.toString());
				}
				return;
			}
		}
		
		// Add to queue.
		_receivedData.add(data);
	}
	
	/**
	 * @return the pending received data.
	 */
	public Queue<byte[]> getReceivedData()
	{
		return _receivedData;
	}
	
	/**
	 * @return the pending read <b>byte[]</b>.
	 */
	public byte[] getPendingData()
	{
		return _pendingData;
	}
	
	/**
	 * Set the pending read <b>byte[]</b>.
	 * @param pendingData the pending read <b>byte[]</b>.
	 */
	public void setPendingData(byte[] pendingData)
	{
		_pendingData = pendingData;
	}
	
	/**
	 * @return the expected pending packet size.
	 */
	public int getPendingPacketSize()
	{
		return _pendingPacketSize;
	}
	
	/**
	 * Set the expected pending packet size.
	 * @param pendingPacketSize the expected packet size.
	 */
	public void setPendingPacketSize(int pendingPacketSize)
	{
		_pendingPacketSize = pendingPacketSize;
	}
	
	/**
	 * @return the writable packet queue waiting to be sent.
	 */
	public Queue<WritablePacket> getSendPacketQueue()
	{
		return _sendPacketQueue;
	}
	
	/**
	 * Sends a packet over the network using the default encryption.
	 * @param packet The packet to send.
	 */
	public void sendPacket(WritablePacket packet)
	{
		_sendPacketQueue.add(packet);
	}
	
	/**
	 * @return the Encryption of this client.
	 */
	public EncryptionInterface getEncryption()
	{
		return null;
	}
	
	/**
	 * Checks if the connection is established.
	 * @return {@code true} if the socket is not {@code null} and therefore assumed to be connected, {@code false} otherwise.
	 */
	public boolean isConnected()
	{
		return _socket != null;
	}
	
	/**
	 * @return the Socket of this client.
	 */
	public Socket getSocket()
	{
		return _socket;
	}
	
	/**
	 * @return the InputStream of this client.
	 */
	public InputStream getInputStream()
	{
		return _inputStream;
	}
	
	/**
	 * @return the OutputStream of this client.
	 */
	public OutputStream getOutputStream()
	{
		return _outputStream;
	}
	
	/**
	 * Checks if the client is currently in the process of sending data.
	 * @return {@code true} if the client is sending data, {@code false} otherwise.
	 */
	public boolean isSending()
	{
		return _isSending.get();
	}
	
	/**
	 * Sets the sending state of the client.
	 * @param value the sending state to set. {@code true} if the client is sending data, {@code false} otherwise.
	 */
	public void setSending(boolean value)
	{
		_isSending.set(value);
	}
	
	/**
	 * Checks if the client is currently in the process of running a client packet.
	 * @return {@code true} if the client is running a client packet, {@code false} otherwise.
	 */
	public boolean isRunning()
	{
		return _isRunning.get();
	}
	
	/**
	 * Sets the running state of the client.
	 * @param value the sending state to set. {@code true} if the client is running a client packet, {@code false} otherwise.
	 */
	public void setRunning(boolean value)
	{
		_isRunning.set(value);
	}
	
	/**
	 * @return the network configurations of this client.
	 */
	public NetConfig getNetConfig()
	{
		return _netConfig;
	}
	
	/**
	 * @return the IP address of this client.
	 */
	public String getIp()
	{
		return _ip;
	}
	
	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append(" - IP: ");
		sb.append(_ip);
		sb.append("]");
		return sb.toString();
	}
}
