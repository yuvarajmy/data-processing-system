package com.example.dataproc;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.*;

public class TaskQueue {
    private static final Logger LOGGER = Logger.getLogger(TaskQueue.class.getName());
    private final BlockingQueue<Task> queue = new LinkedBlockingQueue<>();

    public void addTask(Task task) {
        try { queue.put(task); }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.log(Level.SEVERE, "Interrupted adding task", e);
        }
    }

    public Task getTask() throws InterruptedException {
        return queue.take();
    }
}
