package task2;

import task1.BrokerImpl;


/*The class is similar to the class Broker from task1 but this time, this will return MessageQueues instead of Channels.*/
abstract class QueueBroker {
	
	public final BrokerImpl broker;
	
	/*Initialisation of a queueBroker*/
	QueueBroker(BrokerImpl broker) {
		this.broker = broker;
	}
	
	/*@return the name of the queue broker*/
	abstract String name();
	
	/*Accepts the connection with another queueBroker
	 * @param port : the port number of the connection
	 * @return the MessageQueue created for the connection*/
	abstract MessageQueue accept(int port) throws Exception;
	
	/*Ask for a connection with another queue broker 
	 * @param name : the name of the queue broker we want to connect with
	 * @param port : port used for the connection
	 * @return the MessageQueue created for the connection*/
	abstract MessageQueue connect(String name, int port) throws Exception;
}
