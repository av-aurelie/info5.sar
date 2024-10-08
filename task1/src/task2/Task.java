package task2;
import task1.BrokerImpl;

abstract class Task {
	
	private BrokerImpl broker;
    private QueueBroker queueBroker;
    private Runnable runnable;
    
	private static final ThreadLocal<Task> currentTask = new ThreadLocal<>();
	
	/* Creates a task, associates it with the broker b.
	 * @param b : the broker to associate
	 * @param r : the runnable that will be run
	 * @return the task created */
	Task(BrokerImpl b, Runnable r) {
		this.broker = b;
		this.runnable = r;
		currentTask.set(this);
		
	}
	
	/* Creates a task, associates it with the QueueBroker b.
	 * @param b : the QueueBroker to associate
	 * @param r : the runnable that will be run
	 * @return the task created */
	Task(QueueBroker b, Runnable r) {
		this.queueBroker = b;
		this.runnable = r;
		currentTask.set(this);
	}
	
	/*Same as task1, used to get a broker
	 * @return the Broker got*/
	public BrokerImpl getBroker() {
		return broker;
	}
	
	/*Same as get broker but for a QueueBroker 
	 * @Return the queue broker got*/
	public QueueBroker getQueueBroker() {
		return queueBroker;
	}
	
	/* @return the task associated to the running thread */
	public static Task getTask() {
		return currentTask.get();
	}
}
