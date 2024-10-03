package task1;

public class EchoServer {

    private final Broker broker;
    private final int port;
    private boolean running;

    public EchoServer(Broker broker, int port) {
        this.broker = broker;
        this.port = port;
        this.running = true; // Server is running initially
    }

    public void start() {
        new Thread(() -> {
            while (running) {
                try {
                    System.out.println("Server: Waiting for a new connection on port " + port + "...");

                    // Accept a new connection
                    ChannelImpl channel = (ChannelImpl) broker.accept(port);

                    if (channel != null) {
                        System.out.println("Server: Connection accepted from client.");

                        // Handle the connection in a separate thread to allow multiple clients
                        new Thread(() -> {
                            try {
                                handleClient(channel);
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
    //never used for the moment
    public void stop() {
        System.out.println("Server: Stopping...");
        running = false; // Set the flag to stop the server
    }

    /*
     * Handles the interaction between a client and the server
     * 
     * @param channel used by the client to send the information
     */
    private static void handleClient(ChannelImpl channel) throws InterruptedException, Exception {
        System.out.println("Server: Handling client connection...");

        byte[] buffer = new byte[256];
        CircularBuffer circularBuffer = new CircularBuffer(256);

        while (channel.exitCo) {
            int bytesRead = channel.read(buffer, 0, buffer.length);

            for (int i = 0; i < bytesRead; i++) {
                circularBuffer.push(buffer[i]);
            }

            // Echoes back the data
            channel.write(circularBuffer.m_bytes, 0, bytesRead);
        }

        System.out.println("Server: Client disconnected.");
        channel.disconnect();
    }
}
