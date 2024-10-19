package task3;


public class TestListenerImpl {

}

class ConnectorListenerImpl implements QueueBroker.ConnectListener {

	private String[] sendMessages;
    
    public ConnectorListenerImpl (String[] send) {
    	this.sendMessages = send;
    }

	@Override
	public void connected(MessageQueue queue) {
		System.out.println("Connection established from connector.");
		for (var msg : sendMessages) {
            queue.send(new Message(msg.getBytes()));
        }

	}

	@Override
	public void refused() {
		System.err.println("Connection refused.");
	}
}

class MessageQueueListenerImpl implements MessageQueue.Listener {
	private MessageQueueMixed messageQueue;

	public MessageQueueListenerImpl(MessageQueueMixed mq) {
		this.messageQueue = mq;
	}

	@Override
	public void received(byte[] msg) {
		messageQueue.send(new Message(msg));
		 String messageStr = new String(msg);
		System.out.println("Sent: " + messageStr);

	}

	@Override
	public void sent(Message msg) {
		System.out.println("Message sent from acceptor.");
	}

	@Override
	public void closed() {
		System.out.println("Message queue closed.");
	}
}