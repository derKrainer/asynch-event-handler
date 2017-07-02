package definitions;

import java.util.Map;

/**
 * Definition of an Event which can be handled by the {@link IEventHandler}
 * 
 * @author Kenny
 *
 */
public interface IEvent {

	/**
	 * The name of the event, needed for
	 * {@link IEventHandler#register(IEventConsumer, String...)} to know which
	 * consumers to inform
	 * 
	 * @return the name of the event, should be self-describing like "onClick" /
	 *         "on_key_pressed" / "errorEvent"
	 */
	String getEventName();

	/**
	 * The timestamp of when this event got created
	 * 
	 * @return the time in milliseconds of when this event got created
	 */
	long getTimeStamp();

	/**
	 * Optional event parameters which could have been passed to this event
	 * 
	 * @return event parameters
	 */
	Map<String, Object> getParams();	
}