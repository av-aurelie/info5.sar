package task1;

abstract class Broker {
	
	public String name;
	
	Broker(String name) {
		this.name = name;
	}
	
	abstract Channel accept(int port) throws Exception;
	
	abstract Channel connect(String name, int port) throws Exception;
	
}
