package definitions;

public interface IEventHandler {
	
	/**
	 * Registers a new Event Consumer to listen to a number of given events
	 * @param consumer the consumer to handle events with the given names
	 * @param toListenTo a list of event names the consumer is interested in
	 * @return true if the consumer could be added, false if not
	 */
	boolean register(IEventConsumer consumer, String... toListenTo);
	
	/**
	 * Removes a given consumer from a number of given events
	 * @param consumer the consumer to be removed
	 * @param toUnregisterFrom the events the consumer will be removed from
	 * @return true if at least one consumer has been removed
	 */
	boolean unregister(IEventConsumer consumer, String... toUnregisterFrom);
	
	/**
	 * Triggers an event. This will lead to this handler checking all registers listeners for the triggered event type
	 * and if there are interested consumers, their {@link IEventConsumer#handleEvent(IEvent)} will be called with the
	 * parameter event
	 * @param toHandle the event to be handled
	 * @return the {@link EventHandlingResult} produced by all consumers and combined TODO: define combination rules
	 */
	EventHandlingResult triggerEvent(IEvent toHandle);
}
