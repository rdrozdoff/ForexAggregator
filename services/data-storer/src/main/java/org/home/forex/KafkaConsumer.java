package org.home.forex;

import org.home.forex.model.Quote;
import org.home.forex.service.QuoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    @Autowired
    private QuoteService quoteService;

    @KafkaListener(topics = "${storer.topic.input}")
    public void consume(Quote quote) {
        quoteService.saveQuote(quote);
    }
}