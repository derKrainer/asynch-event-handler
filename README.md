# asynch-event-handler
Java support for asynchronous event handling

Use this handler to Register and fire events and chose if the event should be handled in a synchronous or asynchronous way.

# usage
implement the IEventConsumer interface in your listener class.
Use EventHandlerFactory.getAsynchronousEventHandler or EventHandlerFactory.getSynchronousEventHandler and class IEventHandler.register() to register your EventConsumer to the Event which you are interested in. You can also user EventHandlerFactory.registerGlobally() to register
your listener to all known EventHandlers, though this will most likely rarely make sense.
You should know if you want to use Synchronous or Asynchronous Handling and choose only one.

# SYNCH vs ASYNCH Event handling
- _Asynch Handling_:
	This should be used when you just want to inform all Entities which could potentially be interested in this event. The normal code execution will continue and the different results of the EventListeners will not affect the other listeners as they are all started in paralell.
- _Synchronous handling_:
	This should be used when the eventListeners should be able to influence one another and the EventHandlingResult should be considered between calling two listeners (STOP or ERROR will halt the calling of future listeners)