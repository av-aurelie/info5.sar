package task1;

/*This class is used to test the broker using the echo server.
 * It simulates client sending an array of bytes to the echo server via Channel. */

public class TestClient {
	
	//Number of the port used for the connection
	private static int PORT = 8080;
	
	public void main() {
		
		Broker broker = Task.getBroker();
		
		/*start communication*/
		Channel channel = broker.connect("EchoServer", PORT);
		
		// sequence from 1 to 255 sent from the client to the server
		for (int i = 1; i<255; i++) {
			
			byte[] dataSent = new byte[] {(byte) i};
			channel.write(dataSent, 0, dataSent.length);
			
			byte[] buffer = new byte[1];
			int nbBytes = channel.read(buffer, 0, buffer.length);
			
			// Check if the server echoes correctly 
			if( buffer[0] == i && nbBytes == 1) {
				System.out.print("Byte " + i + " OK ! ");
			}
			else {
				System.out.print("Byte " + i + " KO !!! ");
			}
			
			
		}
		
		/*stop communication*/
		channel.disconnect();
	}

}
