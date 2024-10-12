# Design of the code : 

# Threaded channels :

## Class Broker : 

The broker needs to keep track of the ongoing accept list since it can handle multiple tasks simultaneously. This class must be thread-safe for connect and accept operations.  
A broker can find other brokers using another class called `BrokerManager`.


### accept (port)
```
Channel accept(int port)
	-> list <accept>
	-> rdv
```

We have to be able to block an accept if the broker is waiting for a connect (in a rendez-vous). 


## Rendez-vous object : 


Rendezvous system: two brokers will connect on a port using  `connect`  and  `accept`.  `Connect`  is used on one broker and  `accept`  on the other. The rendezvous is identified through  `accept`.  
The rendezvous is an object. The first broker to arrive creates the rendezvous. The second broker also creates the rendezvous. Once both brokers arrive, the rendezvous ends.

A rendezvous (on a port) cannot be created by multiple accepts (error). Multiple connects can create a rendezvous on the same port. If several rendezvous are created by connects on the same port, the accept will go to the first created rendezvous. After this rendezvous ends, the accept will go to the second one, and so on.  
A map of lists is required to manage this.

```
class RendezVous {

	Broker ba, bc;
	int port; 
	Channel accept (Broker b){
		this.ba = b;
	}
	
	Channel connect (Broker b){
		this.bc = b;
	}
}
```





## Class BrokerManager : 

The `BrokerManager` is given to brokers to avoid the singleton pattern, static variables, and global variables.  
Since the `Broker` class is thread-safe, the `BrokerManager` is also thread-safe.


## Class Channel : 

A `CircularBuffer` is unidirectional, which is why two circular buffers are needed so that two brokers can communicate in both directions. Each broker will be able to write to the buffer simultaneously.
Each buffer is aliased from both sides and the aliasing is crossed ( -> if it used by a broker to write on one side, then it will be used by the other broker to read on the other side). 

### Disconnect 

When there is a disconnection from on side of the buffer, there is a checking to see if the buffer is empty. If it's not empty, the broker that is reading needs to still read until the buffer is empty and then disconnect from its side. 


# Message queue : 

## Class MessageQueue :

## Class QueueBroker :

The QueueBroker class functionment is similar to the class Broker but instead of working with bytes and channels, it will work with MessageQueue and messages (tabl of bytes). 



### Method  String  name() :

As we implement a QueueBroker with a Broker, we can just take the same name for both. Then this method will just return the name of the broker. 

### Method MessageQueue  accept(int  port) :

Same method as in task1. So we will just use the method from BrokerImpl and cast it to be a MessageQueue. As messageQueue use channel, it should work. 


### Method MessageQueue  conenct(String  name, int  port) :

As for accept, we will use the method from task1 and cast the result. 



## Class MessageQueue :

Two mutex need to be initialized here to be used in methods send and receive (one mutex by method). 

```
Semaphore mutexSend = new Semaphore(1);
Semaphore mutexReceive = new Semaphore(1);
```

The connection will be made with a channel.

As the length of a message is not fixed, we will send an int at the beginning of the array so we'll be able to know how many bytes we need to read or to write. 

### Method void  send(byte[]  bytes,  int  offset,  int  length) :
Use of a **mutex** to avoid several threads to write at the same time. If several threads write at the same time, it could corrupt the message. 

At the beginning of the method, we'll put an acquire() for the mutex. The mutex is released at the end of the method. 

The channel used for the MessageQueue will write byte by byte the message (use of the method *write* from the task1). 

### Method byte[]  receive() : 
We will also use a mutex for the same reason as the precedent method. 

As for the other method, the mutex will need to been acquired at the beginning and then released at the end of the method. 

The channel used for the MessageQueue will read byte by byte the message (use of the method *read* from the task1). 


### Method void  close() :

As the connection is made with a channel, we need to disconnect the channel to close tu MessageQueue. 

### Method void  closed() :

We need to know if the channel is disconnected or not. 


# Mixed execution: threaded channels and event-oriented queues :

## Queue broker : 


The `QueueBroker` class functions similarly to the `Broker` class but handles **MessageQueues** instead of channels. It establishes and manages the communication between brokers through message queues. The key difference is that the `QueueBroker` will now use an **event-driven** approach, relying on listeners to handle connection events.

### Listeners interfaces :

The `QueueBroker` will have two types of listeners: `AcceptListener` and `ConnectListener`. These listeners react to connection events: when another broker tries to accept or connect. When a connection request is detected, the listener invokes the appropriate method (`accept` or `connect`) to establish the connection.

Synchronized blocks or mutex as in task 2 (for the message queue) will be needed to access to shared ressources (where we write and read for example) safely.



## Message queue : 

As the queueBroker, the job of a message queue is similar to the one from the task 2: Managing the  transmission of messages between two `QueueBroker` instances.

But this time, this will use a `Listener` to handle events like message reception or queue closure.

As in the previous design, two mutexes are used to synchronize access to the send and receive operations, preventing multiple threads from writing or reading simultaneously.

As for the precedent implementation too, an integer is sent at the start of each message to indicate its length, ensuring that the receiving end knows how many bytes to read.

### Event handling :

MessageQueue will have listeners that react to message reception and queue closure events.

When the listener that reacts to message reception (`received`) will be triggered, it will invoke the `receive`method to read the message. 

When the listener `closed`will be triggered, it will ensure that all messages in the queue will be processed before closing the canal. 

## Event pump : 

We will implement an event pump to process event tasks. 
We will create an interface `event` for events. We can have only one event at once so we'll have a queue, FIFO, to register events. 

In the event interface, we'll also have a method `void react()` thaht will be called when there is an event in the queue. 

We use only one event pump for all the program, so no need to concurrency in this part of the code.

### Event interface :

Each event will implement the `Event` interface.

## Executor : 

This is used to execute events from several tasks. 
We need to use only one executor to avoid concurrency. 

*NB : If we used several executor, it would be "true parallelism.
NB2 : Several are rarely used when we are in programmation oriented events.*

We have this in the executor as the executor is a FIFO queue of events : 
`LinkedList <Runnable> queue;`

We'll have a synchronized method `post(Runnable r)`in this class that will 


This method has to be synchronized as we are in a mixed implementation. If it was only event oriented, we would not need to put a sync because post would not be able to be called concurentially. 