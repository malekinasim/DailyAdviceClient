package com.nasim.example.DailyAdviceClient.controller;

import com.nasim.example.DailyAdviceClient.event.AdviceEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class AdviceStreamController {

    private final AdviceEventPublisher publisher;

    public AdviceStreamController(AdviceEventPublisher publisher) {
        this.publisher = publisher;
    }

    @GetMapping(value = "/api/advice-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamAdvice() {
        return publisher.subscribe();
    }
}