package task2;

import task1.BrokerImpl;
import task1.CircularBuffer;

public class EchoServer {

	

	private final QueueBroker broker;
	private final int port;
	private boolean running;

	public EchoServer(QueueBroker broker, int port) {
		this.broker = broker;
		this.port = port;
		this.running = true; // Server is running initially
	}

	public void start() {
		new Thread(() -> {
			while (running) {
				try {
					System.out.println("Server: Waiting for a new connection on port " + port + "...");

					// Accept a new connection via MessageQueue
					MessageQueue messageQueue = broker.accept(port);

					if (messageQueue != null) {
						System.out.println("Server: Connection accepted from client.");

						// Handle the connection in a separate thread to allow multiple clients
						new Thread(() -> {
							try {
								handleClient(messageQueue);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}).start(); // A new thread is started for each client
					} else {
						System.out.println("Server: No connection accepted.");
					}

				} catch (Exception e) {
					System.out.println("Server: Exception during connection acceptance.");
					e.printStackTrace();
				}
			}
		}).start();
	}

	/* Stops the server from running */
	public void stop() {
		System.out.println("Server: Stopping...");
		running = false; // Set the flag to stop the server
	}

	/*
	 * Handles the interaction between a client and the server
	 * 
	 * @param messageQueue used by the client to send the information
	 */
	private static void handleClient(MessageQueue messageQueue) throws InterruptedException {
		System.out.println("Server: Handling client connection...");

		CircularBuffer circularBuffer = new CircularBuffer(256);

		while (!messageQueue.closed()) {
			// Receive the message from the queue 
			byte[] message = messageQueue.receive();

			// If message is null, it means the connection has closed
			if (message == null) {
				break;
			}

			// Push the received bytes to the circular buffer
			for (byte b : message) {
				circularBuffer.push(b);
			}

			// Echoes back the data 
			messageQueue.send(circularBuffer.m_bytes, 0, circularBuffer.m_bytes.length);
		}

		System.out.println("Server: Client disconnected.");
		messageQueue.close();
	}
}