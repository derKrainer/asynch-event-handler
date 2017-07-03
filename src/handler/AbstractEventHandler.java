package handler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import definitions.IEventConsumer;
import definitions.IEventHandler;

public abstract class AbstractEventHandler implements IEventHandler
{

	protected Map<String, Set<IEventConsumer>> listeners;

	protected AbstractEventHandler()
	{
		this.listeners = new HashMap<>();
	}

	@Override
	public boolean register(IEventConsumer consumer, String... toListenTo)
	{
		if (consumer == null || toListenTo == null || toListenTo.length == 0)
		{
			return false;
		}

		boolean retVal = false;
		for (String evName : toListenTo)
		{
			if (evName != null)
			{
				if (this.listeners.get(evName) == null)
				{
					this.listeners.put(evName, new HashSet<>());
				}
				retVal = this.listeners.get(evName).add(consumer);
			}
		}
		return retVal;
	}

	@Override
	public boolean unregister(IEventConsumer consumer, String... toUnregisterFrom)
	{
		if (consumer == null || toUnregisterFrom == null || toUnregisterFrom.length == 0)
		{
			return false;
		}

		boolean retVal = false;
		for (String unregister : toUnregisterFrom)
		{
			if (unregister != null && this.listeners.get(unregister) != null)
			{
				retVal = retVal || this.listeners.get(unregister).remove(consumer);
			}
		}
		return retVal;
	}

}
