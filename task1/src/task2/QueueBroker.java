package task2;

abstract class QueueBroker {
	abstract QueueBroker(Broke broker);
	abstract String name();
	abstract MessageQueue accept(int port);
	abstract MessageQueue connect(String name, int port);
}
