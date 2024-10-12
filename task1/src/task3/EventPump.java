package task3;

abstract class EventPump {
	
	

	interface Event() {
		void react();
	}
	
	/*Posts an event to be processed by the event pump*/
	abstract void post();
	
	/*Starts the event pump and so begins the process of handling posted events from the queue.*/
	abstract void start();
	
	/*Stops the event pump*/
	abstract void kill();
}
