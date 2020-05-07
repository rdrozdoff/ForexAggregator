package org.home.forex;

import org.home.forex.model.Quote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

    private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);

    @Autowired
    private KafkaTemplate<String, Quote> kafkaTemplate;

    public void publish(Quote quote) {
        log.debug("Sending quote '{}'", quote.toString());
        kafkaTemplate.sendDefault(quote);
    }
}