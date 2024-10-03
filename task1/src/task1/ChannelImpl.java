package task1;

public class ChannelImpl extends Channel {

	// Name of the broker
	String bName;

	// as entrance is where the broker connected to the channel writes, exit is
	// where the
	// other broker is connected and reads
	ChannelImpl exit;
	String exitName;
	boolean connected;
	boolean exitCo; // if exit is not co, then this will soon be disco too

	int port;
	CircularBuffer buffer = new CircularBuffer(256);

	protected ChannelImpl(Broker b, int port) {
		super(b);
		this.bName = b.name;
		this.port = port;
		this.connected = true;

	}

	public int write(byte[] bytes, int offset, int length) throws Exception {

		if (!connected)
			throw new Exception("write() : The broker is not connected ! ");

		int nbytes = 0;
		if (buffer.full()) {
			synchronized (buffer) {
				while (buffer.full()) {
					if (!connected)
						throw new InterruptedException();
					if (!exitCo)
						return length;
					try {
						buffer.wait();
					} catch (InterruptedException e) {
						// nothing to do
					}
				}
			}

		}

		while (nbytes < length && !buffer.full()) {
			byte val = bytes[nbytes + offset];
			buffer.push(val);
			nbytes++;
		}

		if (nbytes != 0) {
			synchronized (buffer) {
				buffer.notify();
			}
		}
		return nbytes;

	}

	public int read(byte[] bytes, int offset, int length) throws InterruptedException {

		if (disconnected())
			throw new InterruptedException();
		int nbytes = 0;
		try {

			if (exit.buffer.empty()) { // buffer where exit writes & our channel reads
				synchronized (exit.buffer) {
					while (exit.buffer.empty()) {
						if (!connected || !exitCo) {
							try {
								exit.buffer.wait();
							} catch (InterruptedException e) {
								// nothing to do
							}
						}
					}
				}
			}

			while (nbytes < length && !(exit.buffer.empty())) {
				byte val = exit.buffer.pull();
				bytes[offset + nbytes] = val;
				nbytes++;
			}
			if (nbytes != 0) {
				synchronized (exit.buffer) {
					exit.buffer.notify();
				}
			}
		} finally {

		}

		return nbytes;
	}

	@Override
	public synchronized void disconnect() {

		if (this.connected) {
			this.connected = false;
			this.exit.exitCo = false;
			notifyAll();
		}

		// if disconnected, nothing to do
		return;
	}

	public void connect(ChannelImpl c, String name) {
		this.exit = c;
		exit.exit = this;
		this.exitCo = exit.connected;
		exit.exitCo = this.connected;
		this.exitName = name;

	}

	boolean disconnected() {
		return !this.connected;
	}

}
