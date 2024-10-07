package task2;

import task1.BrokerImpl;

/*Same tests as test 2 and 3 from task1, modified for the message queue. */

public class MQTest {

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
        QueueBroker queueBroker = new QueueBroker(connector);

        // Task 1: Connecting the connector before the acceptor is ready to accept
        var task1 = new Thread(() -> {
            try {
                MessageQueue queue = queueBroker.connect("acceptor", port);
                System.out.println("Connection established from connector.");
                
                // Sending all messages from testMessage
                for (String message : testMessage) {
                    queue.send(message.getBytes(), 0, message.length());
                    System.out.println("Sent: " + message);
                }

            } catch (Exception e) {
                System.err.println("Error in task1 (test1) (connect): " + e.getMessage());
                e.printStackTrace();
            }
        });

        // Task 2: Accepting the connection after the connector has tried to connect
        var task2 = new Thread(() -> {
            try {
                MessageQueue queue = queueBroker.accept(port);
                System.out.println("Connection accepted by acceptor.");
                
                // Receiving all messages
                for (int i = 0; i < testMessage.length; i++) {
                    byte[] received = queue.receive();
                    System.out.println("Acceptor received: " + new String(received));
                }

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
        QueueBroker queueBroker = new QueueBroker(acceptor);
        int port = 8080;

        // Task 1: Accepting a connection before the connector attempts to connect
        var task1 = new Thread(() -> {
            try {
                MessageQueue queue = queueBroker.accept(port);
                System.out.println("Connection accepted by acceptor.");
                
                // Receiving all messages
                for (int i = 0; i < testMessage.length; i++) {
                    byte[] received = queue.receive();
                    System.out.println("Acceptor received: " + new String(received));
                }

            } catch (Exception e) {
                System.err.println("Error in task1 (test2) (accept): " + e.getMessage());
                e.printStackTrace();
            }
        });

        // Task 2: Connecting after the acceptor has already started accepting
        var task2 = new Thread(() -> {
            try {
                MessageQueue queue = queueBroker.connect("acceptor", port);
                System.out.println("Connection established from connector.");
                
                // Sending all messages from testMessage
                for (String message : testMessage) {
                    queue.send(message.getBytes(), 0, message.length());
                    System.out.println("Sent: " + message);
                }

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
