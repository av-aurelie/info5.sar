package task2;


/*This class is replacing the channel system from the precedent task. 
 * MessageQueue is a FIFO queue of message. Each message is represented as an array
 * of bytes. */
abstract class MessageQueue {

	/*
	 * this method is used to send a message in the queue. This is a blocking method
	 * so this will wait until there is enough free space to send the entire message. 
	 *
	 * @param bytes : array of bytes that we want to send
	 * 
	 * @param int offset : where to start the sending of bytes in the array (between
	 * 0 and length of array - 1)
	 * 
	 * @param int length : number of bytes we want to send
	 * 
	 * @return true if the sending of the bytes has been successfully, false either
	 */
	abstract void send(byte[] bytes, int offset, int length);

	
	/*This method is used to receive a message from the queue. It is also a blocking
	 * method that will wqait until there is a message in the queue to read. 
	 * @return a list of byte that is the message byte by byte
	 * */
	abstract byte[] receive();

	
	/*Closes the MessageQueue and by extension the connection between the two queue 
	 *brokers. After the connection closed, no more messages can be received or sent.*/
	abstract void close();

	
	/*To know if the connection is closed
	 * @return true if the connection is closed and false either */
	abstract boolean closed();
}
