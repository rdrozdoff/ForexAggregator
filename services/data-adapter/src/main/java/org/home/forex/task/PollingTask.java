package org.home.forex.task;

import lombok.extern.slf4j.Slf4j;
import org.home.forex.KafkaProducer;
import org.home.forex.model.Quote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class PollingTask {

    @Value("${feed.baseUrl}")
    private String baseUrl;

    @Autowired
    private KafkaProducer producer;

    private WebClient webClient;

    @PostConstruct
    void init() {
        webClient = WebClient.create(baseUrl);
    }

    @Scheduled(fixedRateString = "${feed.pollingRate}")
    public void pollQuotes() {
        webClient.get()
                .uri("/quotes")
                .retrieve()
                .bodyToMono(Quote.class)
                .subscribe(quote -> producer.publish(quote));
    }
}
