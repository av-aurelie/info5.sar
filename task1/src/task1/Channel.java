package task1;


/* Communication channel, point-to-point stream of bytes. 
 * A connected channel is FIFO and lossless. 
 * This is used to establish a full-duplex communication. 
 * A channel is either connected or disconnected. 
 * It is created connected and it becomes disconnected when either side requests a disconnect. 
 * There is no notion of the end of stream for a connected stream. 
 * To mark the end of a stream, the corresponding channel is simply disconnected.*/


abstract class Channel {
	
	
	/*To read data from a bytes array strarting at the given offset.
	 * If 0 is about to be returned, the method blocks until there is things to read. 
	 * The end of stream is the same as being as the channel being disconnected.
	 * @param bytes is bytes array containing the data that will be read
	 * @param offset the place number in the arrays where the reading will start
	 * @param lengh the number max of bytes that will be read 
	 * @return the number of bytes read   */

	public Channel(Broker b) {
		// TODO Auto-generated constructor stub
	}


	abstract int read(byte[] bytes, int offset, int length) throws InterruptedException;
	
	
	/*To write data from a bytes array strating at the given offset.
	 * Write one byte at a time in the stream. 
	 * Can't return 0 or negative numbers. If 0 is about to be returned, it blocks until there is 
	 * a progression. 
	 * Blocks if there is no room to write any byte. 
	 * If it's invoked on a disconnected channel or if the channel disconnects while writing, 
	 * it will return an error. 
	 * @param bytes is bytes array where the data will be written
	 * @param offset the place number in the arrays where the writing will start
	 * @param lengh the number max of bytes that will be written 
	 * @return the number of bytes written */
	
	abstract int write(byte[] bytes, int offset, int length) throws Exception;
	
	
	/*Disconnect the channel if connected, nothing if it is already disconnected.
	 * The local side will be disconnected only once the remote side has been disconnected
	 * ans there are no more in-transit bytes to read.
	 * The channel can be disconnected at any time and from either side.*/
	
	abstract void disconnect();
	
	
	/*Check the connection status of a channel.
	 * @return true if disconnected and false if it's not.*/
	
	abstract boolean disconnected();

}
