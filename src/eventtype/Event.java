/**
 * 
 */
package eventtype;

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
	
	public Event() {
		this.createdTimestamp = System.currentTimeMillis();
	}

	/* (non-Javadoc)
	 * @see definitions.IEvent#getEventName()
	 */
	@Override
	public String getEventName() {
		return EVENT_NAME;
	}

	/* (non-Javadoc)
	 * @see definitions.IEvent#getTimeStamp()
	 */
	@Override
	public long getTimeStamp() {
		return this.createdTimestamp;
	}
	
	@Override
	public String toString() {
		return Logger.concat("TimeStamp: ", Long.toString(this.getTimeStamp()), "; EventName: ", this.getEventName());
	}
}
