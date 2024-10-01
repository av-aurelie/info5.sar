package task1;

public class RendezVous {

	Broker ba, bc;
	int port;
	ChannelImpl ca, cc;

	//TODO suppress this method if works weel without 
	// channel communication for two brokers
	/*public synchronized void createChannelsForRdv() {

		if (ca == null && cc == null) {

			// Channel acceptor
			//ca = new ChannelImpl();
			ca.bName = ba.name;

			// channel connector
			//cc = new ChannelImpl();
			cc.bName = bc.name;

			ca.exit = cc;
			cc.exit = ca;

		}
	}*/
	
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
			notify();
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
			notify();
		}
		else
			_wait();
		return ca;
		
	}

}
