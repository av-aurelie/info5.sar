package task1;

abstract class Channel {
	
	abstract int read(byte[] bytes, int offset, int length) throws InterruptedException;
	abstract int write(byte[] bytes, int offset, int length) throws Exception;
	abstract void disconnect();
	abstract boolean disconnected();
}
