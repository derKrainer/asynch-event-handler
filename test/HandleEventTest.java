import java.util.HashMap;
import java.util.Map;

import definitions.EventHandlingResult;
import definitions.IEvent;
import definitions.IEventConsumer;
import definitions.IEventHandler;
import eventtype.Event;
import handler.EventHandlerFactory;

/**
 * Dummy test class, should be implemented with JUnit
 * 
 * @author Kenny
 *
 */
public class HandleEventTest
{

	public static void main(String[] args)
	{
		HandleEventTest test = new HandleEventTest();
		test.testEventHandler();
		test.testAsynchEventHandler();
		test.testAllHandlers();
		test.testRegisterGlobally();
	}

	public void testEventHandler()
	{
		IEventHandler handler = EventHandlerFactory.getSynchronousEventHandler();

		final IEventConsumer persistentConsumer = new PersistentConsumer("persistent 1");
		final IEventConsumer persistentConsumer2 = new PersistentConsumer("persistent 2");
		handler.register(new TestConsumer(), Event.EVENT_NAME);
		handler.register(persistentConsumer, Event.EVENT_NAME);
		handler.register(persistentConsumer2, Event.EVENT_NAME);

		EventHandlingResult res = EventHandlerFactory.getSynchronousEventHandler().triggerEvent(new Event());

		if (res == EventHandlingResult.STOP)
		{
			System.out.println("Synchronous Test successfull");
		}
		else
		{
			System.err.println("HandlingResult was not stop");
		}

		EventHandlerFactory.getSynchronousEventHandler().unregister(persistentConsumer, Event.EVENT_NAME);
		EventHandlerFactory.getSynchronousEventHandler().unregister(null, Event.EVENT_NAME, "foobar");
		EventHandlerFactory.getSynchronousEventHandler().unregister(persistentConsumer2, "foobar", "asdf");
	}

	public void testAsynchEventHandler()
	{
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

		EventHandlerFactory.fireGlobalEvent(new Event(new HashMap<>()));
	}

	public void testRegisterGlobally()
	{

		final String evName = "myTestEvent";
		EventHandlerFactory.registerGlobally(new TestConsumer(), evName);
		for (int i = 0; i < 10; i++)
		{
			EventHandlerFactory.registerGlobally(new PersistentConsumer("pers" + 1), evName);
		}

		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("eventName", evName);
		paramMap.put("dummyParam", this);
		EventHandlerFactory.fireGlobalEvent(new Event(evName, paramMap));
		EventHandlerFactory.getAsynchronousEventHandler().triggerEvent(new Event(evName));
		EventHandlerFactory.getAsynchronousEventHandler().triggerEvent(new Event(evName));
	}
}

class TestConsumer implements IEventConsumer
{

	@Override
	public EventHandlingResult handleEvent(IEvent event)
	{
		System.out.println("Handling: " + event.getEventName());
		return EventHandlingResult.CONTINUE;
	}
}

class PersistentConsumer implements IEventConsumer
{

	private String name;

	public PersistentConsumer(String name)
	{
		this.name = name;
	}

	@Override
	public EventHandlingResult handleEvent(IEvent event)
	{
		for (int i = 0; i < 5; i++)
		{
			System.out.print(this.name + " is handling " + event.toString());

			if (event.getParams() != null)
			{
				for (String key : event.getParams().keySet())
				{
					System.out.print("\n  passedParam: key=" + key + "; value=" + event.getParams().get(key));
				}
			}
			System.out.println();
			try
			{
				Thread.sleep(1000);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		return EventHandlingResult.STOP;
	}
}
