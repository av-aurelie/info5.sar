package task1;

abstract class Channel {
	
	/*To read data from a bytes array.
	 * @param bytes is bytes array containing the data that will be read
	 * @param offset the place number in the arrays where the reading will start
	 * @param lengh the number max of bytes that will be read 
	 * @return the number of bytes read or -1 if there is an error */
	
	int read(byte[] bytes, int offset, int length);

	
	/*To write data from a bytes array.
	 * @param bytes is bytes array where the data will be written
	 * @param offset the place number in the arrays where the writing will start
	 * @param lengh the number max of bytes that will be written 
	 * @return the number of bytes written or -1 if there is an error*/
	
	int write(byte[] bytes, int offset, int length);

	
	/*Disconnect the channel if connected, nothing if it is already disconnected.*/
	
	void disconnect();
	
	/*To see if the channel is disconnected. 
	 * @return true if disconnected and false if it's not.*/

	boolean disconnected();
}
