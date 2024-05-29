package org.l2j.commons.network;

import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.l2j.commons.threads.ThreadProvider;
import org.l2j.commons.util.CommonUtil;

/**
 * @author Pantelis Andrianakis
 * @since September 7th 2020
 * @param <E> extends NetClient
 */
public class ExecuteThread<E extends NetClient> implements Runnable
{
	protected static final Logger LOGGER = Logger.getLogger(ExecuteThread.class.getName());
	
	// The core pool size for the ThreadPoolExecutor.
	private static final int EXECUTOR_POOL_SIZE = 2;
	
	// ThreadPoolExecutor used to execute tasks concurrently, avoiding delays caused by waiting for a single client.
	private final ThreadPoolExecutor _executor = new ThreadPoolExecutor(EXECUTOR_POOL_SIZE, Integer.MAX_VALUE, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>(), new ThreadProvider("ExecuteThread Executor", Thread.MAX_PRIORITY));
	
	protected final Set<E> _pool;
	protected final PacketHandlerInterface<E> _packetHandler;
	private boolean _idle;
	
	public ExecuteThread(Set<E> pool, PacketHandlerInterface<E> packetHandler)
	{
		_pool = pool;
		_packetHandler = packetHandler;
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
					
					if (client.isRunning())
					{
						continue ITERATE;
					}
					
					final byte[] data = client.getReceivedData().poll();
					if (data == null)
					{
						continue ITERATE;
					}
					
					client.setRunning(true);
					_executor.execute(new ExecuteTask(client, data));
					
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
	
	private class ExecuteTask implements Runnable
	{
		private final E _client;
		private final byte[] _data;
		
		public ExecuteTask(E client, byte[] data)
		{
			_client = client;
			_data = data;
		}
		
		@Override
		public void run()
		{
			if (_client.getEncryption() != null)
			{
				try
				{
					_client.getEncryption().decrypt(_data, 0, _data.length);
				}
				catch (Exception e)
				{
					if (_client.getNetConfig().isFailedDecryptionLogged())
					{
						LOGGER.warning("ExecuteThread: Problem with " + _client + " data decryption.");
						LOGGER.warning(CommonUtil.getStackTrace(e));
					}
					
					_pool.remove(_client);
					_client.disconnect();
					return;
				}
			}
			
			_packetHandler.handle(_client, new ReadablePacket(_data));
			_client.setRunning(false);
		}
	}
}
