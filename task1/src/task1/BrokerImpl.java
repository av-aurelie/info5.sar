package task1;

import java.util.HashMap;
import java.util.Map;

public class BrokerImpl extends Broker {

	public String name;

	// list of accepts
	public Map<Integer, RendezVous> acceptList; 

	public BrokerImpl(String name) {
		super(name);
		this.name = name;
		BrokerManager.addBroker(this);
		acceptList = new HashMap<>();
	}

	@Override
	public Channel accept(int port) throws Exception {
		RendezVous rdv = null;
		synchronized (acceptList) {
			rdv = acceptList.get(port);
			if (rdv != null) {
				throw new IllegalStateException();
			}
			rdv = new RendezVous();

			acceptList.put(port, rdv);
			acceptList.notifyAll();
		}
		return rdv.accept(this, port);
	}

	@Override
	public Channel connect(String name, int port) throws Exception {
		BrokerImpl b = BrokerManager.getBroker(name);
		if (b == null)
			return null;
		return b._connect(this, port);
	}

	private Channel _connect(BrokerImpl b, int port) throws Exception {
		RendezVous rdv = null;
		synchronized (acceptList) {
			rdv = acceptList.get(port);
			while (rdv == null) {
				try {
					acceptList.wait();
				} catch (InterruptedException e) {
					// Do nothing
				}
			}
			rdv = acceptList.get(port);
		}
		acceptList.remove(port);
		return rdv.connect(b, port);
	}

}
