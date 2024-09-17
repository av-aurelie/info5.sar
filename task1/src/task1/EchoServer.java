package task1;


/* Echo server that accepts multiple clients and echoes back what they send */
public class EchoServer extends Task {

	public EchoServer(Broker broker) {
		super(broker, () -> {
			//
			while (true) {
				// Accept a new connection
				Channel channel = broker.accept(8080);

				// Handle the connection in a new thread
				new Thread(() -> HandleClient(channel)).start();
			}
		});
	}

	/*Is used to process interaction between a client and the server
	 * @param channel used by the client to send the information*/
	private static void HandleClient(Channel channel) {

		// Create a buffer with a size of 256 bytes
		byte[] buffer = new byte[256];
		int bytesRead;

		// Read data from the client and echo it back
		while ((bytesRead = channel.read(buffer, 0, buffer.length)) != -1) {
			channel.write(buffer, 0, bytesRead);
		}

		// Close the connection once all messages have been read
		channel.disconnect();

	}
}
