package definitions;

public interface IEventConsumer {

	/**
	 * Handles the current event
	 * 
	 * @param event
	 *            the event to handle
	 * @return the result of this handler and the passed event, default
	 *         {@link EventHandlingResult#CONTINUE}
	 */
	EventHandlingResult handleEvent(IEvent event);
}
