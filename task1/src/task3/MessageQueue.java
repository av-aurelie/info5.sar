package task3;

/*Class to handle asynchronous message transmission. 
 * This is an event-oriented queue. */

abstract class MessageQueue {
	
	
	/*This interface is designed to handle events that occur within the message queue*/
	interface Listener {
		
		/*This is triggered when a message is received by the queue.
		 * @param : the message that was received.*/
		void received(byte[] msg);

		/*This is triggered when a message has been successfully sent. 
		 * The listener can use this event to confirm that the message was sent.
		 * @param : the message sent. */
		void sent(Message msg);

		/*Triggered when the MessageQueue has been closed.*/
		void closed();
	}

	/*Sets a listener for the message queue. The listener will respond to various events like when a message 
	 * is received, sent, or when the queue is closed.
	 * @param : The listener that implements the listener interface.*/
	abstract void setListener(Listener l);

	/*This method sends a message through the queue. 
	 * @param : msg : the message to be sent 
	 * @return true if the message has been successfully sent, false otherwise*/
	abstract boolean send(Message msg);
	
	/*This method is used to receive a message 
	 * @return the message*/
	abstract byte[] receive() throws Exception;

	/*Closes the message queue. After this, no message can be sent or received.*/
	abstract void close();

	/*Checks if the message queue is closed
	 * @return true if it's closed and false otherwise*/
	abstract boolean closed();
}
