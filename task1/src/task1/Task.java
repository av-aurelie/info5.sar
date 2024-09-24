package task1;

import java.util.NoSuchElementException;

abstract class Task extends Thread {

	private static Broker broker;
	private Runnable task;

	Task(Broker b, Runnable r) {

		this.broker = b;
		this.task = r;

	}

	static Broker getBroker() {
		if (broker != null)
			return broker;
		throw new NoSuchElementException ("getBroker() : There is no broker in this context");
	}
}
