package task3;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class EventPumpImpl extends EventPump {
    
    private final static BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<>();
    private volatile static boolean running = false;
    private static EventPumpImpl instance;
    
    private EventPumpImpl() {
        
    }

    public void post(Event event) {
        eventQueue.add(event); // No need for synchronization
    }
    
    public static synchronized EventPumpImpl getInstance() {
        if (instance == null) {
            instance = new EventPumpImpl();
        }
        return instance;
    }

    @Override
    public void start() {
        running = true;
        new Thread(() -> {
            while (running) {
                try {
                    Event event = eventQueue.take();
                    event.react(); // Process the event
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Preserve interruption status
                    return;
                }
            }
        }).start();
    }

    @Override
    public void kill() {
        running = false;
    }
}

class ReactImpl implements EventPump.Event {
	
    private final Runnable action;

    // Constructor to initialize the action for this event
    public ReactImpl(Runnable action) {
        this.action = action;
    }

    @Override
    public void react() {
        // Execute the action when the event reacts
        action.run();
    }
}
