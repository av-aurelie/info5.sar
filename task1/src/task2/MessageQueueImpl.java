package task2;

import java.util.concurrent.Semaphore;

import task1.Channel;
import task1.ChannelImpl;

public class MessageQueueImpl extends MessageQueue {

	Semaphore mutexSend = new Semaphore(1);
	Semaphore mutexReceive = new Semaphore(1);
	private ChannelImpl channel;

	public MessageQueueImpl(ChannelImpl channel2) {
		this.channel = channel2;
	}

	@Override
	void send(byte[] bytes, int offset, int length) {
	    try {
	        mutexSend.acquire();

	        // Convert the message length into 4 bytes
	        byte[] lengthBytes = new byte[4];
	        lengthBytes[0] = (byte) (length >> 24);
	        lengthBytes[1] = (byte) (length >> 16);
	        lengthBytes[2] = (byte) (length >> 8);
	        lengthBytes[3] = (byte) (length);

	        // Send the message length first
	        int bytesSentForLength = 0;
	        while (bytesSentForLength < 4) {
	            bytesSentForLength += channel.write(lengthBytes, bytesSentForLength, 4 - bytesSentForLength);
	        }

	        // Send the actual message in chunks if necessary
	        int bytesSent = 0;
	        while (bytesSent < length) {
	            int written = 0;
	            while (written < length - bytesSent) {
	                written += channel.write(bytes, offset + bytesSent + written, length - bytesSent - written);
	            }
	            bytesSent += written;
	        }
	    } catch (InterruptedException e) {
	        // Handle interrupted exception (if needed)
	    } catch (Exception e) {
	        // Handle other exceptions (if needed)
	    } finally {
	        mutexSend.release();
	    }
	}



	@Override
	public byte[] receive() {
	    byte[] message = null;

	    try {
	        mutexReceive.acquire();

	        // Read the message length (4 bytes)
	        byte[] lengthBytes = new byte[4];
	        int bytesRead = channel.read(lengthBytes, 0, 4);
	        if (bytesRead < 4) {
	            throw new Exception("Failed to read the full message length.");
	        }

	        // Convert the 4 bytes to an integer representing the message length
	        int messageLength = ((lengthBytes[0] & 0xFF) << 24) |
	                            ((lengthBytes[1] & 0xFF) << 16) |
	                            ((lengthBytes[2] & 0xFF) << 8) |
	                            (lengthBytes[3] & 0xFF);

	        // Allocate a buffer to receive the entire message
	        message = new byte[messageLength];

	        // Read the message in chunks if necessary
	        int totalBytesRead = 0;
	        while (totalBytesRead < messageLength) {
	            int chunkRead = channel.read(message, totalBytesRead, messageLength - totalBytesRead);
	            if (chunkRead == -1) break; // If the channel is closed
	            totalBytesRead += chunkRead;
	        }

	    } catch (InterruptedException e) {
	        e.printStackTrace();
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        mutexReceive.release();
	    }

	    return message;
	}


	@Override
	public void close() {
		this.channel.disconnect();
	}

	@Override
	public boolean closed() {
		return this.channel.disconnected();
	}

}
