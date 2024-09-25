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

	public synchronized Channel connect(Broker b, int port) throws Exception {
		this.bc = b;

		// if null, it's bc the first one, it created the rdv
		if (this.ba == null) {
			try {
				wait();
			} catch (RuntimeException e) {
				throw new RuntimeException("rdv connection failed ! ");
			}

		}

		cc = new ChannelImpl(b, port);
		if(ca != null)
			ca.exit = cc;
		notify();
		return cc;
	}

	public synchronized Channel accept(Broker b, int port) throws Exception {
		this.ba = b;
		if (this.bc == null) {
			try {
				wait();
			} catch (RuntimeException e) {
				throw new RuntimeException("rdv connection failed ! ");
			}
		}

		//createChannelsForRdv();
		notify();
		return ca;
	}

}
