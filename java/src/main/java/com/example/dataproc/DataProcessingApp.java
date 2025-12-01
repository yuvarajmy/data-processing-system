package com.example.dataproc;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataProcessingApp {
    private static final Logger LOGGER = Logger.getLogger(DataProcessingApp.class.getName());

    public static void main(String[] args) {
        TaskQueue queue = new TaskQueue();
        int workers = 4;

        try (ResultWriter writer = new ResultWriter("java_results.txt")) {
            ExecutorService exec = Executors.newFixedThreadPool(workers);

            // 1. Enqueue real tasks
            for (int i = 1; i <= 20; i++) {
                queue.addTask(new Task(i, "payload-" + i));
            }

            // 2. Enqueue poison pills for clean shutdown of workers
            for (int i = 0; i < workers; i++) {
                queue.addTask(new Task(-1, ""));
            }

            // 3. Start worker threads
            for (int i = 1; i <= workers; i++) {
                exec.submit(new Worker(i, queue, writer));
            }

            // 4. Initiate shutdown and WAIT for workers to finish
            exec.shutdown();
            try {
                if (!exec.awaitTermination(60, TimeUnit.SECONDS)) {
                    LOGGER.warning("Timeout waiting for worker threads, forcing shutdownNow().");
                    exec.shutdownNow();
                }
            } catch (InterruptedException e) {
                LOGGER.log(Level.WARNING, "Main thread interrupted while waiting for workers.", e);
                exec.shutdownNow();
                Thread.currentThread().interrupt();
            }

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "IO Error initializing ResultWriter.", e);
        }

        LOGGER.info("Data Processing System (Java) finished. Check java_results.txt for output.");
    }
}
