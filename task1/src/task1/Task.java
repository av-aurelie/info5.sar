package task1;


import java.util.NoSuchElementException;

/*A task is a thread and it's executed by a broker.*/

abstract class Task extends Thread {
	
	/*Create a thread with the broker b. 
	 * @param b broker used for the creation of the task
	 * @param r code to execute
	 * @return the task created */

	private static Broker broker;
	private Runnable task;

	Task(Broker b, Runnable r) {

		this.broker = b;
		this.task = r;

	}

	/*@return the broker linked to the task running*/
	
	static Broker getBroker() {
		if (broker != null)
			return broker;
		throw new NoSuchElementException ("getBroker() : There is no broker in this context");
	}
}

