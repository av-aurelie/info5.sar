package task2;

abstract class Task {
	Task(Broker b, Runnable r) {
	}
	Task(QueueBroker b, Runnable r) {
	}
	abstract Broker getBroker();
	abstract QueueBroker getQueueBroker();
	abstract static Task getTask();
}
