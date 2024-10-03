package task1;

public class RendezVous {

	Broker ba, bc;
	int port;
	ChannelImpl ca, cc;

	private void _wait() {
		while (cc == null || ca == null) {
			try {
				wait();
			}catch (InterruptedException e){
				//do nothing
			}
		}
	}

	public synchronized Channel connect(Broker b, int port) throws Exception {
		this.bc = b;
		cc = new ChannelImpl(b, port);
		if(ca != null) {
			ca.connect(cc, bc.name);
			notifyAll();
		}
		else
			_wait();
		return cc;
	}

	public synchronized Channel accept(Broker b, int port) throws Exception {
		this.ba = b;
		ca = new ChannelImpl(b, port);
		if(cc != null) {
			cc.connect(ca, ba.name);
			notifyAll();
		}
		else
			_wait();
		return ca;
		
	}

}
