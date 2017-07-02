/**
 * 
 */
package eventtype;

import java.util.HashMap;
import java.util.Map;

import definitions.IEvent;
import log.Logger;

/**
 * Most general Event, all events should inherit from this
 * 
 * @author Kenny
 */
public class Event implements IEvent
{
	public static final String EVENT_NAME = "GeneralEvent";

	protected final long createdTimestamp;
	protected final Map<String, Object> params;
	protected final String eventName;

	public Event()
	{
		this(EVENT_NAME, new HashMap<>());
	}

	public Event(String eventName)
	{
		this(eventName, new HashMap<>());
	}

	public Event(Map<String, Object> parameters)
	{
		this(EVENT_NAME, parameters);
	}

	public Event(String eventName, Map<String, Object> parameters)
	{
		super();

		this.eventName = eventName == null ? eventName : EVENT_NAME;
		this.params = parameters == null ? new HashMap<>() : parameters;
		this.createdTimestamp = System.currentTimeMillis();
	}

	@Override
	public String getEventName()
	{
		return EVENT_NAME;
	}

	@Override
	public long getTimeStamp()
	{
		return this.createdTimestamp;
	}

	@Override
	public String toString()
	{
		return Logger.concat("TimeStamp: ", Long.toString(this.getTimeStamp()), "; EventName: ", this.getEventName());
	}

	@Override
	public Map<String, Object> getParams()
	{
		return this.params;
	}
}
