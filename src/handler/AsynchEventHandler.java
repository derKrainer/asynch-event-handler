package handler;

import definitions.EventHandlingResult;
import definitions.IEvent;
import definitions.IEventConsumer;

public class AsynchEventHandler extends AbstractEventHandler {
	
	public static final int THREAD_POOL_SIZE = 8;
	
	private EventHandlingRunner[] threadPool;

	protected AsynchEventHandler() {
		super();
		
		this.threadPool = new EventHandlingRunner[THREAD_POOL_SIZE];
	}

	@Override
	public EventHandlingResult triggerEvent(IEvent toHandle) {
		
		if(this.listeners.get(toHandle.getEventName()) != null)
		{
			int idx = 0;
			for(IEventConsumer cons : this.listeners.get(toHandle.getEventName()))
			{
				this.threadPool[idx] = new EventHandlingRunner(cons, toHandle);
				new Thread(threadPool[idx++]).start();
				
				if(idx >= THREAD_POOL_SIZE) {
					idx = idx % THREAD_POOL_SIZE;
					// TODO: start check for which threads are finished and fill the pool there
				}
			}
		}
		this.threadPool = new EventHandlingRunner[THREAD_POOL_SIZE];
		
		return EventHandlingResult.CONTINUE;
	}
}

class EventHandlingRunner implements Runnable
{
	private final IEventConsumer cons;
	private final IEvent toConsume;
	
	public EventHandlingRunner(IEventConsumer consumer, IEvent toConsume) {
		this.cons = consumer;
		this.toConsume = toConsume;
	}

	@Override
	public void run() {
		System.out.println("Handling event: " + this.toConsume.getEventName());
		this.cons.handleEvent(toConsume);
	}
	
}