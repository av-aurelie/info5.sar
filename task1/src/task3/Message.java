package task3;

/*This class is used to define a message. A Message is a byte array sent by a MessageQueue to a QueueBroker.*/
public class Message {
	/*Message content*/
	byte[] bytes;
	/* Where we want to start to read the message */
	int offset;
	/*Length of the message*/
	int length;
	
	public Message(byte[] b, int o, int length){
		this.bytes = b;
		this.offset = o;
		this.length = length;
	}
	
	
	public Message(byte[] b){
		this.bytes = b;
		this.offset = 0;
		this.length = b.length;
	}
}
