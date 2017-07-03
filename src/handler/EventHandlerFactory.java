package handler;

import java.util.HashMap;
import java.util.Map;

import definitions.EventHandlingResult;
import definitions.IEvent;
import definitions.IEventConsumer;
import definitions.IEventHandler;

public class EventHandlerFactory {

	public enum EventHandlerTypes {
		SYNCHRONOS,

		ASYNCHRONOS
	}
	
	/**
	 * Ordered list of all Types in the order they should be returned by getAllHandlers
	 */
	public static final EventHandlerTypes[] ALL_TYPES_ORDERED = { EventHandlerTypes.ASYNCHRONOS, EventHandlerTypes.SYNCHRONOS };

	private static Map<EventHandlerTypes, IEventHandler> instances = new HashMap<>();

	public static IEventHandler getEventHandlerForType(EventHandlerTypes type) {
		if (type == EventHandlerTypes.SYNCHRONOS) {
			synchronized (instances) {
				if (instances.get(EventHandlerTypes.SYNCHRONOS) == null) {
					instances.put(EventHandlerTypes.SYNCHRONOS, new EventHandler());
				}
				return instances.get(EventHandlerTypes.SYNCHRONOS);
			}
		} else if (type == EventHandlerTypes.ASYNCHRONOS) {
			synchronized (instances) {
				if (instances.get(EventHandlerTypes.ASYNCHRONOS) == null) {
					instances.put(EventHandlerTypes.ASYNCHRONOS, new AsynchEventHandler());
				}
				return instances.get(EventHandlerTypes.ASYNCHRONOS);
			}
		}
		throw new IllegalStateException("Unknown EventHandlerType: " + type);
	}

	/**
	 * Retrieves a list of all known event handlers (one for each entry in
	 * {@link EventHandlerTypes} )
	 * 
	 * @return all event handlers
	 */
	public static IEventHandler[] getAllHandlers() {
		IEventHandler[] retVal = new IEventHandler[ALL_TYPES_ORDERED.length];

		int idx = 0;
		for (EventHandlerTypes type : ALL_TYPES_ORDERED) {
			retVal[idx++] = getEventHandlerForType(type);
		}

		return retVal;
	}
	
	/**
	 * Triggers {@link IEventHandler#triggerEvent(IEvent)} of all known event handlers 
	 * @param toFire the event to be fired
	 * @return TODO: a way to signal all
	 */
	public static EventHandlerTriggerResult[] fireGlobalEvent(IEvent toFire) {
		EventHandlerTriggerResult[] retVal = new EventHandlerTriggerResult[ALL_TYPES_ORDERED.length];
		
		int idx = 0;
		for(IEventHandler handler : getAllHandlers())
		{
			EventHandlingResult result = handler.triggerEvent(toFire);
			retVal[idx++] = new EventHandlerTriggerResult(handler, result);
		}
		
		return retVal;
	}
	
	/**
	 * Registers a consumer to all known event handlers
	 * @param consumer the consumer of potential events
	 * @param eventTypes all event types which the consumer is interested in
	 */
	public static void registerGlobally(IEventConsumer consumer, String... eventTypes)
	{
		IEventHandler[] allHandlers = getAllHandlers();
		for(IEventHandler handler : allHandlers)
		{
			handler.register(consumer, eventTypes);
		}
	}

	public static IEventHandler getSynchronousEventHandler() {
		return getEventHandlerForType(EventHandlerTypes.SYNCHRONOS);
	}

	public static IEventHandler getAsynchronousEventHandler() {
		return getEventHandlerForType(EventHandlerTypes.ASYNCHRONOS);
	}
}