package task1;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class BrokerManager {
	
	static Map<String, BrokerImpl> brokers = new HashMap<>();
	
	public synchronized BrokerImpl getBroker (String name) {
		BrokerImpl broker = brokers.get(name);
		if(broker != null)
			return broker;
		throw new NoSuchElementException("The broker " + name + "doesn't exist");
	}
	
	public static synchronized void addBroker (BrokerImpl broker) {
		brokers.put(broker.name, broker);
	}
	
	public synchronized void removeBroker (BrokerImpl broker) {
		brokers.remove(broker.name);
	}

}
