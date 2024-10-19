package task3;

import java.util.LinkedList;

public class ExecutorImpl extends Executor {
    private final LinkedList<Runnable> queue = new LinkedList<>();

    @Override
    public void run() {
        while (true) {
            Runnable task;
            synchronized (queue) {
                while (queue.isEmpty()) {
                    try {
                        queue.wait(); // Wait for tasks
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return; // Exit on interrupt
                    }
                }
                task = queue.poll();
            }
            if (task != null) {
                task.run(); // Execute the task
            }
        }
    }

    @Override
    public void post(Runnable r) {
        synchronized (queue) {
            queue.add(r);
            queue.notify(); // Notify the processing thread
        }
    }
}
