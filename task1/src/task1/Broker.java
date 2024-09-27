package task1;


/*
 * Representation of a broker. 
 * A broker is a middleware enableling services to communicate with each others 
 * by routing and delivering messages.*/

/* There is no precedence between connect and accept, this is a symmetrical rendez-vous: 
 * the first operation waits for the second one. 
 * Both accept and connect operations are therefore blocking calls, 
 * blocking until the rendez-vous happens, 
 * both returning a fully connected and usable full-duplex channel. */

abstract class Broker {
	
	public String name;
	
	
	/*Creation of a new broker. 
	 * The broker is identify by its name. 
	 * Name has to be unique. 
	 * @param : name of the broker*/
	
	Broker(String name) {
		this.name = name;
	}
	
	/*Creation of a channel to enable the communication. 
	 * @param : port to listen on.
	 * @return : the channel representing the connection for the communication. 
	 * Will return null if there is a problem. */
	
	abstract Channel accept(int port) throws Exception;
	
	
	/*Establish a channel. 
	 * connect block until there is a matching accept. 
	 * @param name of the broker
	 * @param port of an accept on the remote broker name
	 * @return the channel that has been constructed and connected or null if the name of the remote
	 * broker is not found. */

	abstract Channel connect(String name, int port) throws Exception;
	

}
