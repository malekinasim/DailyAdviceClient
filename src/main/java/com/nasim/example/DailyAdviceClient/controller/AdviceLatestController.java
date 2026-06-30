package com.nasim.example.DailyAdviceClient.controller;


import com.nasim.example.DailyAdviceClient.store.AdviceStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ConditionalOnProperty(name = "advice.client.mode", havingValue = "api")
public class AdviceLatestController {

    private final AdviceStore adviceStore;

    public AdviceLatestController(AdviceStore adviceStore) {
        this.adviceStore = adviceStore;
    }

    @GetMapping("/api/advice/latest")
    public String getLatestAdvice() {
        return adviceStore.getLatestAdvice();
    }
}