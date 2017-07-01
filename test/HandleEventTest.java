import definitions.EventHandlingResult;
import definitions.IEvent;
import definitions.IEventConsumer;
import definitions.IEventHandler;
import eventtype.Event;
import handler.EventHandlerFactory;

public class HandleEventTest {
	
	public static void main(String[] args) {
		HandleEventTest test = new HandleEventTest();
		test.testEventHandler();
		test.testAsynchEventHandler();
		test.testAllHandlers();
	}
	
	public void testEventHandler() {
		IEventHandler handler = EventHandlerFactory.getSynchronousEventHandler();
		
		handler.register(new TestConsumer(), Event.EVENT_NAME);
		handler.register(new PersistentConsumer("persistent 1"), Event.EVENT_NAME);
		handler.register(new PersistentConsumer("persistent 2"), Event.EVENT_NAME);
		
		EventHandlingResult res = EventHandlerFactory.getSynchronousEventHandler().triggerEvent(new Event());
		
		if(res == EventHandlingResult.STOP) {
			System.out.println("Synchronous Test successfull");
		}
		else {
			System.err.println("HandlingResult was not stop");
		}
	}
	
	public void testAsynchEventHandler() {
		IEventHandler handler = EventHandlerFactory.getAsynchronousEventHandler();
		
		handler.register(new TestConsumer(), Event.EVENT_NAME);
		handler.register(new PersistentConsumer("persistent 1"), Event.EVENT_NAME);
		handler.register(new PersistentConsumer("persistent 2"), Event.EVENT_NAME);
		
		EventHandlerFactory.getAsynchronousEventHandler().triggerEvent(new Event());
	}
	
	public void testAllHandlers()
	{
		EventHandlerFactory.getAsynchronousEventHandler().register(new TestConsumer(), Event.EVENT_NAME);
		EventHandlerFactory.getAsynchronousEventHandler().register(new PersistentConsumer("asynch1"), Event.EVENT_NAME);
		EventHandlerFactory.getAsynchronousEventHandler().register(new PersistentConsumer("asynch2"), Event.EVENT_NAME);
		
		EventHandlerFactory.getSynchronousEventHandler().register(new TestConsumer(), Event.EVENT_NAME);
		EventHandlerFactory.getSynchronousEventHandler().register(new PersistentConsumer("synch1"), Event.EVENT_NAME);
		EventHandlerFactory.getSynchronousEventHandler().register(new PersistentConsumer("synch2"), Event.EVENT_NAME);
		
		EventHandlerFactory.fireGlobalEvent(new Event());
	}
}


class TestConsumer implements IEventConsumer {

	@Override
	public EventHandlingResult handleEvent(IEvent event)
	{
		System.out.println("Handling: " + event.getEventName());
		return EventHandlingResult.CONTINUE;
	}
}

class PersistentConsumer implements IEventConsumer {
	
	private String name;
	
	public PersistentConsumer(String name) {
		this.name = name;
	}
	@Override
	public EventHandlingResult handleEvent(IEvent event) {
		for(int i = 0; i < 5; i++) {
			System.out.println(this.name + " is handling " + event.toString());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return EventHandlingResult.STOP;
	}
}
