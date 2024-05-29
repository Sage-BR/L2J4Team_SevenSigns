package org.l2j.commons.network;

import java.io.OutputStream;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.l2j.commons.threads.ThreadProvider;

/**
 * @author Pantelis Andrianakis
 * @since June 22nd 2023
 * @param <E> extends NetClient
 */
public class SendThread<E extends NetClient> implements Runnable
{
	// The core pool size for the ThreadPoolExecutor.
	private static final int EXECUTOR_POOL_SIZE = 2;
	
	// ThreadPoolExecutor used to execute tasks concurrently, avoiding delays caused by waiting for a single client.
	private final ThreadPoolExecutor _executor = new ThreadPoolExecutor(EXECUTOR_POOL_SIZE, Integer.MAX_VALUE, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>(), new ThreadProvider("SendThread Executor", Thread.MAX_PRIORITY));
	
	protected final Set<E> _pool;
	private boolean _idle;
	
	public SendThread(Set<E> pool)
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
					if (!client.isConnected())
					{
						_pool.remove(client);
						continue ITERATE;
					}
					
					if (client.isSending())
					{
						continue ITERATE;
					}
					
					final Queue<WritablePacket> packetQueue = client.getSendPacketQueue();
					if (packetQueue.isEmpty())
					{
						continue ITERATE;
					}
					
					client.setSending(true);
					_executor.execute(new SendTask(client, packetQueue));
					
					_idle = false;
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
	
	private class SendTask implements Runnable
	{
		private final E _client;
		private final Queue<WritablePacket> _packetQueue;
		
		public SendTask(E client, Queue<WritablePacket> packetQueue)
		{
			_client = client;
			_packetQueue = packetQueue;
		}
		
		@Override
		public void run()
		{
			WritablePacket writablePacket;
			final OutputStream outputStream = _client.getOutputStream();
			while ((writablePacket = _packetQueue.poll()) != null)
			{
				final byte[] sendableBytes = writablePacket.getSendableBytes(_client.getEncryption());
				if (sendableBytes == null)
				{
					continue;
				}
				
				try
				{
					// Send the packet data.
					outputStream.write(sendableBytes);
					outputStream.flush();
					
					// Run packet implementation.
					writablePacket.run();
				}
				catch (Exception e)
				{
					_pool.remove(_client);
					_client.disconnect();
					break;
				}
			}
			
			_client.setSending(false);
		}
	}
}
