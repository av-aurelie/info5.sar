package task3;

import java.util.HashMap;
import java.util.Map;

import task1.BrokerImpl;
import task2.MessageQueueImpl;
import task2.QueueBrokerImpl;

public class QueueBrokerMixed extends QueueBroker {
    private final Map<Integer, AcceptListener> listeners;
    private final String name;
    private final QueueBrokerImpl broker;
    private final Executor excutor;
    private EventPumpImpl eventPump;

    public QueueBrokerMixed(String name) {
        super(name);
        this.name = name;
        this.listeners = new HashMap<>();
        BrokerImpl b = new BrokerImpl(name);
        this.broker = new QueueBrokerImpl(b);
        this.excutor = new ExecutorImpl();
        this.eventPump = EventPumpImpl.getInstance();
    }

    @Override
    public boolean bind(int port, AcceptListener listener) {
        if (listeners.containsKey(port)) 
        	return false; // Binding failed
        listeners.put(port, listener); // Store the listener

		// Create a task to handle the connection in the threaded environment
        this.excutor.post( () -> _handleConnection(port, listener));
        new Thread(() -> excutor.run()).start();
        return true; // Binding successful
    }

    private void _handleConnection(int port, AcceptListener listener) {
        while (true) {
            try {
                MessageQueueImpl msgsQueue = (MessageQueueImpl)broker.accept(port);
                
                MessageQueueMixed eventQueue = new MessageQueueMixed(msgsQueue);
                eventPump.post(new ReactImpl(() -> listener.accepted(eventQueue)));
            } catch (Exception e) {
                break; // Exit if an error occurs
            }
        }
    }


    @Override
    public boolean unbind(int port) {
        return listeners.remove(port) != null; // Remove the listener if it exists
    }

    @Override
    public boolean connect(String name, int port, ConnectListener listener) {
    	MessageQueue queue;
		try {

			queue = new MessageQueueMixed ((MessageQueueImpl) broker.connect(name, port));
			eventPump.post(new ReactImpl(() -> listener.connected(queue))); // Notify the listener about the connection
	        return true; // Return true if connection was successful
		} catch (Exception e) {
			System.err.println("Connection failed to " + name + " on port " + port);
			eventPump.post(new ReactImpl(() -> listener.refused()));
			return false;
		}
        
    }
}
