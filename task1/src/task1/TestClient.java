package task1;

/* This class is used to test the broker using the echo server.
 * It simulates client sending an array of bytes to the echo server via Channel. */

public class TestClient {

    // Number of the port used for the connection
    private static final int PORT = 8080;

    public static void main(String[] args) throws Exception {

        // Creation of a new broker
        BrokerImpl broker = new BrokerImpl("EchoServer");

        // Initialize and start the echo server
        EchoServer server = new EchoServer(broker);

        // Start the server task using an anonymous subclass of Task
        Task serverTask = new Task(broker, () -> {
            try {
                server.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }) {
            @Override
            public void run() {
                task.run();
            }
        };
        serverTask.start();

        // Give the server some time to start up
        Thread.sleep(1000);

        // Connect the client to the broker (EchoServer)
        Channel channel = broker.connect("EchoServer", PORT);

        // Sequence from 1 to 255 sent from the client to the server
        for (int i = 1; i < 255; i++) {
            byte[] dataSent = new byte[]{(byte) i};
            channel.write(dataSent, 0, dataSent.length);

            byte[] buffer = new byte[1];
            int nbBytes = channel.read(buffer, 0, buffer.length);

            // Check if the server echoes correctly
            if (buffer[0] == i && nbBytes == 1) {
                System.out.println("Byte " + i + " OK! Echoed correctly.");
            } else {
                System.err.println("Byte " + i + " KO!!! Echo mismatch.");
            }
        }

        // Stop communication
        channel.disconnect();
        System.out.println("Test complete. Disconnected.");
    }
}
