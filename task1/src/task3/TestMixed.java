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
    	EventPumpImpl pump = EventPumpImpl.getInstance();
        pump.start();
        BrokerImpl acceptor = new BrokerImpl("acceptor");
        int port = 8080;
        QueueBroker qConnector = new QueueBrokerMixed("connector");
        QueueBroker qAcceptor = new QueueBrokerMixed("acceptor");

        // Task 1: Connecting the connector before the acceptor is ready to accept
        var task1 = new Thread(() -> {
            try {
                Thread.sleep(500); // Introduce a small delay for the acceptor to start later
                qConnector.connect(acceptor.name, port, new ConnectorListenerImpl(testMessage));
                
            } catch (Exception e) {
                System.err.println("Error in task1 (test1) (connect): " + e.getMessage());
                e.printStackTrace();
            }
        });

        // Task 2: Accepting the connection after the connector has tried to connect
        var task2 = new Thread(() -> {
            try {
                qAcceptor.bind(port, queue -> {
                    queue.setListener(new MessageQueueListenerImpl((MessageQueueMixed) queue));
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
    	EventPumpImpl pump = EventPumpImpl.getInstance();
        pump.start();
        QueueBroker queueBroker = new QueueBrokerMixed("acceptor");
        int port = 8080;

        // Task 1: Accepting a connection before the connector attempts to connect
        var task1 = new Thread(() -> {
            try {
                queueBroker.bind(port, queue -> {
                    queue.setListener(new MessageQueueListenerImpl((MessageQueueMixed) queue));
                });

            } catch (Exception e) {
                System.err.println("Error in task1 (test2) (accept): " + e.getMessage());
                e.printStackTrace();
            }
        });

        // Task 2: Connecting after the acceptor has already started accepting
        var task2 = new Thread(() -> {
            try {
            	Thread.sleep(500); 
                queueBroker.connect("acceptor", port, new ConnectorListenerImpl(testMessage));
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
