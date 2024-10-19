package task3;

class Task {
    private Runnable runnable;

    // Constructor
    Task(Runnable r) {
        this.runnable = r;
    }

    // Starts the task processing
    public void start() {
        new Thread(runnable).start(); // Start in a new thread
    }
    
    public void stop() {
        // Implement stop logic if necessary
    }
}