package org.home.forex.task;

import lombok.extern.slf4j.Slf4j;
import org.home.forex.KafkaProducer;
import org.home.forex.model.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.net.URISyntaxException;

@Component
@Slf4j
public class PollingTask {

    @Value("${feed.url}")
    private URI feedUrl;

    @Autowired
    private KafkaProducer producer;

    private WebClient webClient = WebClient.create();

    @Scheduled(fixedRateString = "${feed.pollingRate}")
    public void pollQuotes() throws URISyntaxException {
        webClient
            .get()
            .uri(feedUrl)
            .retrieve()
            .bodyToMono(Quote.class)
            .subscribe(quote -> producer.publish(quote));
    }
}
