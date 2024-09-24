package task1;

import java.util.HashMap;
import java.util.Map;

public class BrokerImpl extends Broker {

	public String name;

	// list of rdv
	public Map<Integer, RendezVous> rdvList;

	public BrokerImpl(String name) {
		super(name);
		BrokerManager.addBroker(this);
		rdvList = new HashMap<>();
	}

	public synchronized Channel accept(int port) throws Exception {

		// is the rdv already created ?
		if (rdvList.containsKey(port)) {
			// is an accept already on this port ?
			if (rdvList.get(port).ba != null)
				throw new Exception("accept : an accept is already on the port :" + port);

		}
		// creation of the rdv
		else
			rdvList.put(port, new RendezVous());

		RendezVous rdv = rdvList.get(port);
		Channel rdvChannel = rdv.accept(this);

		// End of the rdv
		rdvList.remove(port);
		BrokerManager.brokers.notifyAll();
		return rdvChannel;

	}

	public synchronized Channel connect(String name, int port) throws Exception {

		// if the rdv is not created yet
		if (!rdvList.containsKey(port)) {
			rdvList.put(port, new RendezVous());
		}
		RendezVous rdv = rdvList.get(port);
		Channel rdvChannel = rdv.connect(this);

		
		return rdvChannel;

	}

}
