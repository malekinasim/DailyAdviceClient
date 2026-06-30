package com.nasim.example.DailyAdviceClient.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AdviserReaderClient implements AutoCloseable {

    private final SocketChannel socketChannel;
    private final MessageListener messageListener;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private volatile boolean running = false;

    public AdviserReaderClient(SocketChannel socketChannel, MessageListener messageListener) {
        this.socketChannel = socketChannel;
        this.messageListener = messageListener;
    }

    // Console mode: blocks this thread
    public void listenBlocking() throws IOException {
        running = true;

        try {
            listenLoop();
        } finally {
            close();
        }
    }

    // api and stream mode: listens in background thread
    public void startListeningAsync() {
        running = true;

        executor.submit(() -> {
            try {
                listenLoop();
            } catch (IOException e) {
                if (running) {
                    System.out.println("Socket error: " + e.getMessage());
                }
            } finally {
                close();
            }
        });
    }

    private void listenLoop() throws IOException {
        if (socketChannel == null || !socketChannel.isOpen()) {
            System.out.println("Socket channel is not open.");
            return;
        }
        Reader reader = Channels.newReader(socketChannel, StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(reader);

        String message;

        while (running && (message = bufferedReader.readLine()) != null) {
            messageListener.onMessage(message);
        }

        System.out.println("Server closed the connection.");
    }

    @Override
    public void close() {
        running = false;

        try {
            if (socketChannel != null && socketChannel.isOpen()) {
                socketChannel.close();
            }
        } catch (IOException e) {
            System.out.println("Error while closing socket: " + e.getMessage());
        }

        executor.shutdownNow();
    }
}