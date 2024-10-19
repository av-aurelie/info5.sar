package task3;

abstract class EventPump {

    interface Event {
        void react(); // Method that the implementing class must define
    }

    /* Posts an event to be processed by the event pump
     * @param e: the event to add to the pump */
    //abstract void post(Event e);
		
	

    /* Starts the event pump and begins processing posted events from the queue. */
    abstract void start();

    /* Stops the event pump */
    abstract void kill();
}
