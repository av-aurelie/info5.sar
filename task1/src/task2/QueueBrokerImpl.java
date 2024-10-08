package task2;

import task1.BrokerImpl;
import task1.ChannelImpl;

public class QueueBrokerImpl extends QueueBroker {

	
	BrokerImpl broker;

	/* Initialisation of a queueBroker */
	QueueBrokerImpl(BrokerImpl broker) {
		super(broker);
		this.broker = broker;
		
		
	}

	/* @return the name of the queue broker */
	@Override
	public String name() {
		return broker.name;
	}

	/*
	 * Accepts the connection with another queueBroker
	 * 
	 * @param port : the port number of the connection
	 * 
	 * @return the MessageQueue created for the connection
	 */
	@Override
	public MessageQueue accept(int port) throws Exception {
		MessageQueueImpl accepted = new MessageQueueImpl((ChannelImpl) broker.accept(port));
		return accepted;

	}

	/*
	 * Ask for a connection with another queue broker
	 * 
	 * @param name : the name of the queue broker we want to connect with
	 * 
	 * @param port : port used for the connection
	 * 
	 * @return the MessageQueue created for the connection
	 */
	@Override
	public MessageQueue connect(String name, int port) throws Exception {
		MessageQueueImpl connected = new MessageQueueImpl((ChannelImpl) broker.connect(name, port));
		return connected;
	}
}
