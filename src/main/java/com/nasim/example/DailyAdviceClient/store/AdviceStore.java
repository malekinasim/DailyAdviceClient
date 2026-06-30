package com.nasim.example.DailyAdviceClient.store;


import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;

@Component
public class AdviceStore {

    private final AtomicReference<String> latestAdvice =
            new AtomicReference<>("No advice received yet.");

    public void updateAdvice(String advice) {
        latestAdvice.set(advice);
    }

    public String getLatestAdvice() {
        return latestAdvice.get();
    }
}