package handler;

import definitions.EventHandlingResult;
import definitions.IEvent;
import definitions.IEventConsumer;
import log.Logger;

/**
 * Class to call registered event listeners in an asynchronous way
 * 
 * @author Kenny
 *
 */
public class AsynchEventHandler extends AbstractEventHandler
{

	public static final int THREAD_POOL_SIZE = 8;

	private Thread[] threadPool;

	protected AsynchEventHandler()
	{
		this(THREAD_POOL_SIZE);
	}

	protected AsynchEventHandler(int threadPoolSize)
	{
		super();

		this.threadPool = new Thread[threadPoolSize > 0 ? threadPoolSize : THREAD_POOL_SIZE];
	}

	@Override
	public EventHandlingResult triggerEvent(IEvent toHandle)
	{

		if (this.listeners.get(toHandle.getEventName()) != null)
		{
			int idx = 0;
			for (IEventConsumer cons : this.listeners.get(toHandle.getEventName()))
			{
				while (this.threadPool[idx] != null && (this.threadPool[idx].getState() != Thread.State.TERMINATED))
				{
					idx = (idx + 1) % this.threadPool.length;
				}
				this.threadPool[idx] = new Thread(new EventHandlingRunner(cons, toHandle));
				this.threadPool[idx].start();

				idx++;
				if (idx >= THREAD_POOL_SIZE)
				{
					idx = idx % THREAD_POOL_SIZE;
				}
			}
		}

		// as we cannot guarantee that any of these eventHandlers have already finished and we should not be
		// waiting on them, just return continue
		return EventHandlingResult.CONTINUE;
	}
}

class EventHandlingRunner implements Runnable
{
	private final IEventConsumer cons;
	private final IEvent toConsume;

	public EventHandlingRunner(IEventConsumer consumer, IEvent toConsume)
	{
		this.cons = consumer;
		this.toConsume = toConsume;
	}

	@Override
	public void run()
	{
		Logger.debug("Handling event asynchronously: ", this.toConsume.getEventName());
		this.cons.handleEvent(toConsume);
	}

}