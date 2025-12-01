package com.example.dataproc;

import java.util.concurrent.TimeUnit;
import java.util.logging.*;

public class Worker implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(Worker.class.getName());
    private final int id;
    private final TaskQueue queue;
    private final ResultWriter writer;

    public Worker(int id, TaskQueue queue, ResultWriter writer) {
        this.id = id;
        this.queue = queue;
        this.writer = writer;
    }

    @Override
    public void run() {
        LOGGER.info("Worker " + id + " started.");
        try {
            while (true) {
                Task task = queue.getTask();
                if (task.getId() == -1) break;
                TimeUnit.MILLISECONDS.sleep(200);
                writer.writeLine("Worker " + id + " processed " + task.getPayload());
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Worker error", e);
        }
        LOGGER.info("Worker " + id + " finished.");
    }
}
