package task2;
import task1.BrokerImpl;

abstract class Task {
	
	/* Creates a task, associates it with the broker b.
	 * @param b : the broker to associate
	 * @param r : the runnable that will be run
	 * @return the task created */
	Task(BrokerImpl b, Runnable r) {
	}
	
	/* Creates a task, associates it with the QueueBroker b.
	 * @param b : the QueueBroker to associate
	 * @param r : the runnable that will be run
	 * @return the task created */
	Task(QueueBroker b, Runnable r) {
	}
	
	/*Same as task1, used to get a broker
	 * @return the Broker got*/
	abstract BrokerImpl getBroker();
	
	/*Same as get broker but for a QueueBroker 
	 * @Return the queue broker got*/
	abstract QueueBroker getQueueBroker();
	
	/* @return the task associated to the running thread */
	abstract static Task getTask();
}
