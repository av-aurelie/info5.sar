package task1;

public class ChannelImpl extends Channel{

	// Name of the broker
	String bName;

	// as entrance is where the broker connected to the channel writes, exit is
	// where the
	// other broker is connected and reads
	ChannelImpl exit;
	boolean connected;
	boolean exitCo; //if exit is not co, then this will soon be disco too
	
	int port;
	
	public ChannelImpl(Broker b, int port) {
		this.bName = b.name;
		this.port = port;
	}

	CircularBuffer buffer = new CircularBuffer(256);

	public int write(byte[] bytes, int offset, int length) throws Exception {

		if (!connected)
			throw new Exception("write() : The broker is not connected ! ");

		for (int i = offset; i < length; i++) {
			while (buffer.full()) {
				try {
					wait();
				} catch (InterruptedException e) {
					disconnect();
					return i - offset;
				}
			}
			buffer.push(bytes[i]);
		}

		// something to read for the broker on the other side
		exit.notifyAll();

		return length - offset;
	}

	public int read(byte[] bytes, int offset, int length) {

		for (int i = offset; i < length; i++) {
			try {
				wait();
			} catch (InterruptedException e) {
				disconnect();
				return i-offset;
			}
			bytes[i] = buffer.pull();
		}

		return length - offset;
	}

	public synchronized void disconnect() {

		if (connected) {
			connected = false;
			exit.connected = false;
			notifyAll();
			exit.notifyAll();
		}

		// if disconnected, nothing to do
		return;
	}

	boolean disconnected() {
		return !connected;
	}

}
