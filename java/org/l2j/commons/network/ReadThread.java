package org.l2j.commons.network;

import java.io.InputStream;
import java.util.Set;

/**
 * @author Pantelis Andrianakis
 * @since September 7th 2020
 * @param <E> extends NetClient
 */
public class ReadThread<E extends NetClient> implements Runnable
{
	private final Set<E> _pool;
	private final byte[] _sizeBuffer = new byte[2]; // Reusable size buffer.
	private final byte[] _pendingSizeBuffer = new byte[1]; // Reusable pending size buffer.
	private boolean _idle;
	
	public ReadThread(Set<E> pool)
	{
		_pool = pool;
	}
	
	@Override
	public void run()
	{
		while (true)
		{
			// No need to iterate when pool is empty.
			if (!_pool.isEmpty())
			{
				_idle = true;
				
				// Iterate client pool.
				ITERATE: for (E client : _pool)
				{
					try
					{
						final InputStream inputStream = client.getInputStream();
						if (inputStream == null) // Unexpected disconnection?
						{
							// Null InputStream: client
							onDisconnection(client);
							continue ITERATE;
						}
						
						// Continue when there are no bytes that can be read.
						if (inputStream.available() < 1)
						{
							continue ITERATE;
						}
						
						// Continue read if there is a pending byte array.
						final byte[] pendingData = client.getPendingData();
						if (pendingData != null)
						{
							// Allocate an additional byte array based on pending packet size.
							final int pendingPacketSize = client.getPendingPacketSize();
							final byte[] additionalData = new byte[pendingPacketSize - pendingData.length];
							final int bytesRead = inputStream.read(additionalData);
							switch (bytesRead)
							{
								// Disconnected.
								case -1:
								{
									onDisconnection(client);
									continue ITERATE;
								}
								// Nothing read.
								case 0:
								{
									continue ITERATE;
								}
								// Data was read.
								default:
								{
									// Merge additional read data.
									final int currentSize = pendingData.length + bytesRead;
									final byte[] mergedData = new byte[currentSize];
									System.arraycopy(pendingData, 0, mergedData, 0, pendingData.length);
									System.arraycopy(additionalData, 0, mergedData, pendingData.length, bytesRead);
									
									// Read was complete.
									if (currentSize >= pendingPacketSize)
									{
										// Add received data to client.
										client.addReceivedData(mergedData);
										client.setPendingData(null);
									}
									else
									{
										client.setPendingData(mergedData);
									}
									
									_idle = false;
								}
							}
							continue ITERATE;
						}
						
						// Read incoming packet size (short).
						boolean sizeRead = false;
						switch (inputStream.read(_sizeBuffer))
						{
							// Disconnected.
							case -1:
							{
								onDisconnection(client);
								continue ITERATE;
							}
							// Nothing read.
							case 0:
							{
								continue ITERATE;
							}
							// Need to read two bytes to calculate size.
							case 1:
							{
								// Keep it under 10 attempts (100ms).
								COMPLETE_SIZE_READ: for (int attempt = 0; attempt < 10; attempt++)
								{
									// Wait for pending data.
									try
									{
										Thread.sleep(10);
									}
									catch (Exception ignored)
									{
									}
									
									// Try to read the missing extra byte.
									_pendingSizeBuffer[0] = 0;
									switch (inputStream.read(_pendingSizeBuffer))
									{
										// Disconnected.
										case -1:
										{
											onDisconnection(client);
											continue ITERATE;
										}
										// Nothing read.
										case 0:
										{
											continue COMPLETE_SIZE_READ;
										}
										// Merge additional read byte.
										default:
										{
											_sizeBuffer[1] = _pendingSizeBuffer[0];
											sizeRead = true;
											break COMPLETE_SIZE_READ;
										}
									}
								}
								
								// Read failed.
								if (!sizeRead)
								{
									onDisconnection(client);
									continue ITERATE;
								}
								
								// Fallthrough.
							}
							// Read actual packet bytes.
							default:
							{
								// Allocate a new byte array based on packet size read.
								final int packetSize = calculatePacketSize();
								final byte[] packetData = new byte[packetSize];
								final int bytesRead = inputStream.read(packetData);
								switch (bytesRead)
								{
									// Disconnected.
									case -1:
									{
										onDisconnection(client);
										continue ITERATE;
									}
									// Nothing read.
									case 0:
									{
										continue ITERATE;
									}
									// Send data read to the client packet queue.
									default:
									{
										// Read was not complete.
										if (bytesRead < packetSize)
										{
											final byte[] pendindBytes = new byte[bytesRead];
											System.arraycopy(packetData, 0, pendindBytes, 0, bytesRead);
											client.setPendingData(pendindBytes);
											client.setPendingPacketSize(packetSize);
										}
										else // Add received data to client.
										{
											client.addReceivedData(packetData);
										}
										
										_idle = false;
									}
								}
							}
						}
					}
					catch (Exception e) // Unexpected disconnection?
					{
						onDisconnection(client);
					}
				}
				
				// Prevent high CPU caused by repeatedly looping.
				try
				{
					Thread.sleep(_idle ? 10 : 1);
				}
				catch (Exception ignored)
				{
				}
			}
			else // Remain idle for 1 second.
			{
				// Prevent high CPU caused by repeatedly looping.
				try
				{
					Thread.sleep(1000);
				}
				catch (Exception ignored)
				{
				}
			}
		}
	}
	
	private int calculatePacketSize()
	{
		return ((_sizeBuffer[0] & 0xff) | ((_sizeBuffer[1] << 8) & 0xffff)) - 2;
	}
	
	private void onDisconnection(E client)
	{
		_pool.remove(client);
		client.disconnect();
	}
}
