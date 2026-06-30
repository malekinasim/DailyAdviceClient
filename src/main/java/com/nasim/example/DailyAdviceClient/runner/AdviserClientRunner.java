package com.nasim.example.DailyAdviceClient.runner;

import com.nasim.example.DailyAdviceClient.event.AdviceEventPublisher;
import com.nasim.example.DailyAdviceClient.store.AdviceStore;
import com.nasim.example.DailyAdviceClient.socket.AdviserReaderClient;
import com.nasim.example.DailyAdviceClient.socket.SocketConnection;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.channels.SocketChannel;

@Component
public class AdviserClientRunner implements CommandLineRunner {

    @Value("${dailyAdviser.server.ip}")
    private String serverIP;

    @Value("${dailyAdviser.server.port}")
    private String serverPort;

    private final AdviceStore adviceStore;
    private final AdviceEventPublisher publisher;

    private AdviserReaderClient client;

    public AdviserClientRunner(AdviceStore adviceStore, AdviceEventPublisher publisher) {
        this.adviceStore = adviceStore;
        this.publisher = publisher;
    }

    @Override
    public void run(String... args) {
        try {
            SocketChannel channel = SocketConnection.connect(serverIP, serverPort);

            client = new AdviserReaderClient(channel, message -> {
                System.out.println("Advice received: " + message);

                adviceStore.updateAdvice(message);

                publisher.publish(message);
            });

            client.startListeningAsync();

            System.out.println("Advice client started. Console + API store + SSE stream are active.");

        } catch (IOException e) {
            System.out.println("Could not connect to advice server: " + e.getMessage());
        }
    }

    @PreDestroy
    public void shutdown() {
        if (client != null) {
            client.close();
            System.out.println("Advice client stopped.");
        }
    }
}