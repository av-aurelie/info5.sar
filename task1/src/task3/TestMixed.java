package task3;

import task1.BrokerImpl;

public class TestMixed {
	
    static String[] testMessage = { "Ceci est un message !", "Et ceci un autre message.", "Et encore un !",
            "Jamais trois sans quatre ahah", "Un petit dernier pour la route ..." };
    
    
	public static void main(String[] args) throws InterruptedException {
		test1();
		test2();
	}

	// Test where we have a connect before the accept
    static void test1() {
        BrokerImpl connector = new BrokerImpl("connector");
        BrokerImpl acceptor = new BrokerImpl("acceptor");
        int port = 8080;
        QueueBroker qConnector = new QueueBrokerImpl(connector);
        QueueBroker qAcceptor = new QueueBrokerImpl(acceptor);

        // Task 1: Connecting the connector before the acceptor is ready to accept
        var task1 = new Thread(() -> {
            try {
                Thread.sleep(500); // Introduce a small delay for the acceptor to start later
                qConnector.connect(acceptor.name(), port, new QueueBroker.ConnectListener() {
                    @Override
                    public void connected(MessageQueue queue) {
                        System.out.println("Connection established from connector.");
                        
                        // Send all messages from testMessage
                        for (String message : testMessage) {
                            queue.send(new Message(message.getBytes(), 0, message.length()));
                            System.out.println("Sent: " + message);
                        }
                    }

                    @Override
                    public void refused() {
                        System.err.println("Connection refused.");
                    }
                });
            } catch (Exception e) {
                System.err.println("Error in task1 (test1) (connect): " + e.getMessage());
                e.printStackTrace();
            }
        });

        // Task 2: Accepting the connection after the connector has tried to connect
        var task2 = new Thread(() -> {
            try {
                qAcceptor.bind(port, queue -> {
                    queue.setListener(new MessageQueue.Listener() {
                        @Override
                        public void received(byte[] msg) {
                            System.out.println("Acceptor received: " + new String(msg));
                        }

                        @Override
                        public void sent(Message msg) {
                            System.out.println("Message sent from acceptor.");
                        }

                        @Override
                        public void closed() {
                            System.out.println("Message queue closed.");
                        }
                    });
                });

            } catch (Exception e) {
                System.err.println("Error in task2 (test1) (accept): " + e.getMessage());
                e.printStackTrace();
            }
        });

        // Start both threads
        task1.start();
        task2.start();

        // Join threads to wait for them to complete
        try {
            task1.join();
            task2.join();
        } catch (InterruptedException e) {
            System.out.println("test1: something went wrong");
        }

        System.out.println("test1: completed");
    }

    // Test where we have the accept before the connect
    static void test2() {
        BrokerImpl connector = new BrokerImpl("connector");
        BrokerImpl acceptor = new BrokerImpl("acceptor");
        QueueBroker queueBroker = new QueueBrokerImpl(acceptor);
        int port = 8080;

        // Task 1: Accepting a connection before the connector attempts to connect
        var task1 = new Thread(() -> {
            try {
                queueBroker.bind(port, queue -> {
                    queue.setListener(new MessageQueue.Listener() {
                        @Override
                        public void received(byte[] msg) {
                            System.out.println("Acceptor received: " + new String(msg));
                        }

                        @Override
                        public void sent(Message msg) {
                            System.out.println("Message sent from acceptor.");
                        }

                        @Override
                        public void closed() {
                            System.out.println("Message queue closed.");
                        }
                    });
                });

            } catch (Exception e) {
                System.err.println("Error in task1 (test2) (accept): " + e.getMessage());
                e.printStackTrace();
            }
        });

        // Task 2: Connecting after the acceptor has already started accepting
        var task2 = new Thread(() -> {
            try {
                queueBroker.connect("acceptor", port, new QueueBroker.ConnectListener() {
                    @Override
                    public void connected(MessageQueue queue) {
                        System.out.println("Connection established from connector.");
                        
                        // Send all messages from testMessage
                        for (String message : testMessage) {
                            queue.send(new Message(message.getBytes(), 0, message.length()));
                            System.out.println("Sent: " + message);
                        }
                    }

                    @Override
                    public void refused() {
                        System.err.println("Connection refused.");
                    }
                });
            } catch (Exception e) {
                System.err.println("Error in task2 (test2) (connect): " + e.getMessage());
                e.printStackTrace();
            }
        });

        // Start both threads
        task1.start();
        task2.start();

        // Join threads to wait for them to complete
        try {
            task1.join();
            task2.join();
        } catch (InterruptedException e) {
            System.out.println("test2: something went wrong");
        }

        System.out.println("test2: completed");
    }
}