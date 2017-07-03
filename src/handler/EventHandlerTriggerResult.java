package handler;

import definitions.EventHandlingResult;
import definitions.IEventHandler;

public class EventHandlerTriggerResult
{
	private IEventHandler handler;
	private EventHandlingResult result;

	public EventHandlerTriggerResult(IEventHandler handler, EventHandlingResult result)
	{
		this.handler = handler;
		this.result = result;
	}

	public IEventHandler getHandler()
	{
		return handler;
	}

	public EventHandlingResult getResult()
	{
		return result;
	}
}