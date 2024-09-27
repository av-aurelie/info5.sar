package task1;


/* Echo server that accepts multiple clients and echoes back what they send */
public class EchoServer extends Task {

	public EchoServer(Broker broker) {
		super(broker, () -> {
			//
			while (true) {
				// Accept a new connection
				Channel channel;
				try {
					channel = broker.accept(8080);
					// Handle the connection in a new thread
					new Thread(() -> {
						try {
							HandleClient(channel);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}).start();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				
			}
		});
	}

	/*Is used to process interaction between a client and the server
	 * @param channel used by the client to send the information*/
	private static void HandleClient(Channel channel) throws InterruptedException, Exception {

		// Creation of a circular buffer
		CircularBuffer buffer = new CircularBuffer(256);
		byte[] tempBuffer = new byte[1]; //buffer to hold incoming data

		// Read data from the client and echo it back
		while ((channel.read(tempBuffer, 0, tempBuffer.length)) != -1) {
			buffer.push(tempBuffer[0]);
			byte dataEcho = buffer.pull();
			channel.write( new byte[]{dataEcho}, 0, 1);
		}

		// Close the connection once all messages have been read
		channel.disconnect();

	}
}
