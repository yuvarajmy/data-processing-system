package com.example.dataproc;

import java.io.*;
import java.util.logging.*;

public class ResultWriter implements AutoCloseable {
    private static final Logger LOGGER = Logger.getLogger(ResultWriter.class.getName());
    private final BufferedWriter writer;

    public ResultWriter(String filePath) throws IOException {
        writer = new BufferedWriter(new FileWriter(filePath, false));
    }

    public synchronized void writeLine(String line) {
        try {
            writer.write(line);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error writing line", e);
        }
    }

    @Override
    public void close() {
        try { writer.close(); }
        catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error closing writer", e);
        }
    }
}
