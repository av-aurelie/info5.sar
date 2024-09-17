package task1;

/*A task is a thread and it's executed by a broker.*/

abstract class Task extends Thread {
	
	/*Create a thread with the broker b. 
	 * @param b broker used for the creation of the task
	 * @param r code to execute
	 * @return the task created */
	
	Task(Broker b, Runnable r);
	
	
	/*@return the broker linked to the task running*/

	static Broker getBroker();
}	
