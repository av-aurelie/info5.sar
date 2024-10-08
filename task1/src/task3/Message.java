package task3;

/*This class is used to define a message. A Message is a byte array sent by a MessageQueue to a QueueBroker.*/
class Message {
	/*Message content*/
	byte[] bytes;
	/* Where we want to start to read the message */
	int offset;
	/*Length of the message*/
	int length;
}
