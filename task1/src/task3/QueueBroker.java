package task3;


/*This class is for QueueBroker. The QueueBroker are the same as in task 2 but this time we are in a mixed */
abstract class QueueBroker {
	
	/*Constructor*/
	QueueBroker(String name);

	/*Defines a listener to handle the event when a connection is accepted*/
	interface AcceptListener {
		
		/*Invoked when a connection is accepted. 
		 * @param queue : the MessageQueue that represents the connection that has been accepted.*/
		void accepted(MessageQueue queue);
	}

	/*Binds the QueueBroker to a specific port and listens for connections.
	 * @param the port to bind the QueueBroker to.
	 * @param the listener that will handles the connections.
	 * @return true if it has been bind successfully and false otherwise.*/
	boolean bind(int port, AcceptListener listener);

	/*Unbind the broker from a specific port.
	 * @param : the port we want to unbind the broker to
	 * @return true if it has been successfully unbind and false otherwise*/
	boolean unbind(int port);

	/*Defines a listener to handle the event when a connection is tried by a broker on another broker.*/
	interface ConnectListener {
		/*Is invoked when a connection is made successfully.
		 * @param : queue the connection with the other broker*/
		void connected(MessageQueue queue);

		/*Invoked when a connection is refused*/
		void refused();
	}

	/*Attempts to connect with another queueBroker of name name on the port port.
	 * @param name : the name of the QueueBroker when want to connect with
	 * @param port : the port umber we want to connect on
	 * @param listener : the ConnectListener that handle the result of the connection attempt
	 * @return true if the connection is a success and false otherwise*/
	boolean connect(String name, int port, ConnectListener listener);
}
