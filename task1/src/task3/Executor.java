package task3;

import java.util.LinkedList;

/*This class is used to execute events from several threads */

abstract public class Executor extends Thread {

	/*This queue is FIFO queue*/
	LinkedList <Runnable> queue;
	
	Executor(){
		queue = new LinkedList <Runnable> ();
	}
	/*Runs the first runnable from the queue*/
	abstract public void run();
	
	/*This method is used to put a runnable in the queue
	 * @param runnable r the runnable that will be put in the queue*/
	abstract public void post (Runnable r);
	
	
	
}
