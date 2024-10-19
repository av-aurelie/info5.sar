package task3;

import task2.MessageQueueImpl;
import task2.QueueBrokerImpl;

import java.util.LinkedList;
import java.util.Queue;

import task1.ChannelImpl;

public class MessageQueueMixed extends MessageQueue {

    private final Queue<Message> messagesToSend;
    private Listener listener;
    private boolean closed = false;
    private Task receiver;
    private Task sender;
    private MessageQueueImpl messageQueue;
    private boolean senderRunning = false;
    private EventPumpImpl eventPump;

    public MessageQueueMixed(MessageQueueImpl msgsQueue) {
        super();
        this.messagesToSend = new LinkedList<>();
        this.messageQueue = msgsQueue;

        this.receiver = new Task(receiverTask());
        this.sender = new Task(senderTask());
        this.eventPump = EventPumpImpl.getInstance();
    }

    private Runnable receiverTask() {
        return () -> {
            while (!closed) {
                try {
                    byte[] msg = receive();
                } catch (Exception e) {
                    handleError(e);
                }
            }
        };
    }

    private Runnable senderTask() {
        return () -> {
            while (!closed) {
                try {
                    Message msg;
                    synchronized (messagesToSend) {
                        while (messagesToSend.isEmpty()) {
                            messagesToSend.wait(); // Wait for messages to send
                        }
                        msg = messagesToSend.poll(); // Retrieve the next message
                    }

                    if (msg != null) {
                        sendMessage(msg);
                    }
                } catch (Exception e) {
                    break;
                }
            }
            senderRunning = false; // Reset senderRunning when the task ends
        };
    }

    @Override
    public byte[] receive() throws Exception {
        byte[] message = messageQueue.receive();
        Message msg = new Message(message);
        if (listener != null) {
        	eventPump.post(new ReactImpl(() -> listener.received(msg.bytes)));
        }
        return message;
    }

    private void sendMessage(Message msg) throws Exception {
        messageQueue.send(msg.bytes, msg.offset, msg.length); // Correct usage
    }

    private void handleError(Exception e) {
        if (listener != null) {
        	eventPump.post(new ReactImpl(() -> listener.closed()));
        }
        closed = true;
    }

    @Override
    public void setListener(Listener l) {
        this.listener = l;
        receiver.start(); // Start the receiver task
    }

    @Override
    public boolean send(Message msg) {
        if (closed) return false;
        synchronized (messagesToSend) {
            messagesToSend.add(msg);
            messagesToSend.notify(); // Notify the sender task
            if (!senderRunning) {
                senderRunning = true;
                sender.start(); // Start the sender task
            }
        }
        return true;
    }

    @Override
    public void close() {
        closed = true;
        // Handle stopping of tasks safely
        // sender.stop() and receiver.stop() logic should be implemented
        notifyListenerClosed();
    }

    private void notifyListenerClosed() {
        if (listener != null) {
        	eventPump.post(new ReactImpl(() -> listener.closed()));
        }
    }

    @Override
    public boolean closed() {
        return closed;
    }
}