package handler;

import definitions.EventHandlingResult;
import definitions.IEvent;
import definitions.IEventConsumer;
import log.Logger;

public class EventHandler extends AbstractEventHandler {
	
	@Override
	public EventHandlingResult triggerEvent(IEvent toHandle) {
		EventHandlingResult retVal = EventHandlingResult.CONTINUE;
		if(this.listeners.get(toHandle.getEventName()) != null)
		{
			for(IEventConsumer cons : this.listeners.get(toHandle.getEventName()))
			{
				retVal = cons.handleEvent(toHandle);
				
				if(retVal == EventHandlingResult.ERROR || retVal == EventHandlingResult.STOP) {
					Logger.info("Cancelling event handling due to handling state ", retVal.name());
					break;
				}
			}
		}
		return retVal;
	}

}
