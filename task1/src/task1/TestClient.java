package task1;

/* This class is used to test the broker using the echo server.
 * It simulates multiple clients sending an array of bytes to the echo server via Channel. */

public class TestClient {

	public static void main(String[] args) throws InterruptedException {

		// test1();
		test2();
		test3();
	}

	// test of echoserver
	static void test1() throws InterruptedException {
		Broker broker = new BrokerImpl("EchoServer");

		// Start the Echo server
		EchoServer server = new EchoServer(broker, 8080);
		server.start();

		int numberOfClients = 10;

		// Create and start 10 clients with a 1 second delay between each
		for (int i = 1; i <= numberOfClients; i++) {
			final int clientId = i;
			new Thread(() -> {
				try {
					Broker clientBroker = new BrokerImpl("Client" + clientId);
					Channel clientChannel = clientBroker.connect("EchoServer", 8080);
					System.out.println("Client" + clientId + ": Connected to server!");

					// Example message: each client sends its clientId
					byte[] message = new byte[] { 1, 2, 3, 4, 5 };
					clientChannel.write(message, 0, message.length); // Send message

					// Read the echoed message from the server
					byte[] buffer = new byte[message.length];
					int bytesRead = clientChannel.read(buffer, 0, buffer.length); // Read response

					// Print the echoed bytes
					System.out.println("Client" + clientId + ": Echoed bytes: ");
					for (int j = 0; j < bytesRead; j++) {
						System.out.println("Byte " + j + ": " + buffer[j]);
					}

					// Disconnect the client
					clientChannel.disconnect();
					System.out.println("Client" + clientId + ": Disconnected from server!");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}).start();

			// Wait for 1 second before creating the next client
			Thread.sleep(500);
		}
	}

	// test where we have a connect before the accept
	static void test2() {
		BrokerImpl connector = new BrokerImpl("connector");
		BrokerImpl acceptor = new BrokerImpl("acceptor");
		int port = 8080;
		RendezVous rendezVous = new RendezVous();

		// Task 1: Connecting the connector before the acceptor is ready to accept
		var task1 = new Thread(() -> {
		    try {
		        rendezVous.connect(acceptor, port);
		    } catch (Exception e) {
		        System.err.println("Error in task1 (test2) (connect): " + e.getMessage());
		        e.printStackTrace();
		    }
		});

		// Task 2: Accepting the connection after the connector has tried to connect
		var task2 = new Thread(() -> {
		    try {
		        rendezVous.accept(connector, port);
		    } catch (Exception e) {
		        System.err.println("Error in task2 (test2) (accept): " + e.getMessage());
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
			System.out.println("test2 : something goes wrong");
		}

		System.out.println("test2 : nothing goes wrong");

	}

	/* test where we have the accept before the connect */
	static void test3() {
		BrokerImpl connector = new BrokerImpl("connector");
		BrokerImpl acceptor = new BrokerImpl("acceptor");
		RendezVous rendezVous = new RendezVous();
		int port = 8080;

		// Task 1: Accepting a connection before the connector attempts to connect
		var task1 = new Thread(() -> {
		    try {
		        rendezVous.accept(connector, port);
		    } catch (Exception e) {
		        System.err.println("Error in task1 (test3) (accept): " + e.getMessage());
		        e.printStackTrace();
		    }
		});
		// Task 2: Connecting after the acceptor has already started accepting
		var task2 = new Thread(() -> {
		    try {
		        rendezVous.connect(acceptor, port);
		    } catch (Exception e) {
		        System.err.println("Error in task2 (test3) (connect): " + e.getMessage());
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
			System.out.println("test3 : something goes wrong");
		}

		System.out.println("test3 : nothing goes wrong");
	}

}
