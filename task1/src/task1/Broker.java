package task1;

/*
 * Representation of a broker. 
 * A broker is a middleware enableling services to communicate with each others 
 * by routing and delivering messages.*/

abstract class Broker {
	
	/*Creation of a new broker. 
	 * The broker is identify by its name. 
	 * Name has to be unique. 
	 * @param : name of the broker*/
	
	Broker(String name);
	
	/*Creation of a channel to enable the communication. 
	 * @param : port to listen on. 
	 * @return : the channel representing the connection for the communication. 
	 * Will return null if there is a problem. */

	Channel accept(int port);
	
	/*Establish connection between a broker and a channel. 
	 * @param name of the broker
	 * @param port of the channel.
	 * @return the channel that has been connected*/

	Channel connect(String name, int port);
}
