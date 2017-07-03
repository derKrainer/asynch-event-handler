/**
 * 
 */
package test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import definitions.EventHandlingResult;
import definitions.IEvent;
import definitions.IEventConsumer;
import definitions.IEventHandler;
import eventtype.Event;
import handler.AbstractEventHandler;
import handler.EventHandlerFactory;
import handler.EventHandlerTriggerResult;
import junit.framework.TestCase;

/**
 * @author Kenny
 *
 */
public class EventHandlerTest extends TestCase
{
	static final int NO_CONSUMER = 5;

	protected IEventConsumer[] consumers;

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();

		this.consumers = new IEventConsumer[NO_CONSUMER + 1];
		for (int i = 0; i <= NO_CONSUMER; i++)
		{
			this.consumers[i] = new PersistentConsumer("persistent consumer #" + i);
		}

		this.consumers[NO_CONSUMER] = new TestConsumer();

		for (IEventHandler handler : EventHandlerFactory.getAllHandlers())
		{
			for (IEventConsumer consumer : this.consumers)
			{
				handler.register(consumer, Event.EVENT_NAME, "dummyEvent", "asynchEvent", "foobar");
			}
		}
	}

	/**
	 * Test method for {@link handler.EventHandler#triggerEvent(definitions.IEvent)}.
	 */
	@Test
	public void testTriggerEvent()
	{
		EventHandlingResult res = EventHandlerFactory.getSynchronousEventHandler().triggerEvent(new Event());
		Map<String, Object> testParams = new HashMap<>();
		testParams.put("testParam1", new String[] { "asdf", "fdsa" });
		testParams.put("testParam2", "testParam2");
		testParams.put("testParam3", null);
		testParams.put(null, "foobar");
		EventHandlingResult res2 = EventHandlerFactory.getSynchronousEventHandler().triggerEvent(new Event(testParams));
		EventHandlingResult res3 = EventHandlerFactory.getSynchronousEventHandler().triggerEvent(new Event(null, null));

		assertEquals(res, EventHandlingResult.STOP);
		assertEquals(res2, EventHandlingResult.STOP);
		assertEquals(res3, EventHandlingResult.STOP);
	}

	/**
	 * Test method for {@link handler.AbstractEventHandler#AbstractEventHandler()}.
	 */
	@Test
	public void testAbstractEventHandler()
	{
		assertTrue(EventHandlerFactory.getSynchronousEventHandler() instanceof AbstractEventHandler);
		assertTrue(EventHandlerFactory.getAsynchronousEventHandler() instanceof AbstractEventHandler);
	}

	/**
	 * Test method for {@link handler.AbstractEventHandler#register(definitions.IEventConsumer, java.lang.String[])} and
	 * {@link handler.AbstractEventHandler#unregister(IEventConsumer, String...)}
	 */
	@Test
	public void testRegisterUnregister()
	{
		for (IEventHandler handler : EventHandlerFactory.getAllHandlers())
		{
			testEventHandlerRegisterFunctionality(handler);
		}

		IEventConsumer cons = new TestConsumer();
		EventHandlerFactory.registerGlobally(cons, "globalEvent");
		EventHandlerTriggerResult[] results = EventHandlerFactory.fireGlobalEvent(new Event("globalEvent"));

		for (EventHandlerTriggerResult result : results)
		{
			assertNotNull(result.getHandler());
			assertNotNull(result.getResult());
		}

		for (IEventHandler handler : EventHandlerFactory.getAllHandlers())
		{
			assertFalse(handler.unregister(cons, "not the event you are looking for"));
			assertTrue(handler.unregister(cons, "globalEvent"));
		}

	}

	private void testEventHandlerRegisterFunctionality(IEventHandler handler)
	{
		// registering null eventhandler to null events should not be possible
		assertFalse(handler.register(null, (String[]) null));
		// registering an acutual event handler to null event should not be possible
		assertFalse(handler.register(this.consumers[0], (String[]) null));
		// registering a null handler to an actual event name should not be possible
		assertFalse(handler.register(null, "asdf"));

		// registering existing handler to new event should be possible
		assertTrue(handler.register(this.consumers[0], "testRegisterEvent"));
		// registering existing handler to existing event should not be possible
		assertFalse(handler.register(this.consumers[0], "dummyEvent"));

		// removing existing handler from non existing event should not be possible
		assertFalse(handler.unregister(consumers[0], "notRegisteredEvent"));
		// removing existing handler from non existng event should be possible
		assertTrue(handler.unregister(this.consumers[0], "dummyEvent"));
		// removing non-existing handler from existing event should not be possible
		assertFalse(handler.unregister(new IEventConsumer() {
			@Override
			public EventHandlingResult handleEvent(IEvent event)
			{
				return EventHandlingResult.ERROR;
			}
		}));

		// removing an already removed event handler should not be possible
		assertFalse(handler.unregister(consumers[0], "dummyEvent"));
	}

	@Test
	public void testAsynchEventHandler()
	{
		IEventHandler handler = EventHandlerFactory.getAsynchronousEventHandler();

		EventHandlingResult res = handler.triggerEvent(new Event());

		assertEquals(res, EventHandlingResult.CONTINUE);
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