package org.home.forex;

import lombok.extern.slf4j.Slf4j;
import org.home.forex.model.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaProducer {

    @Value("${feed.topic.output}")
    private String topic;

    @Autowired
    private KafkaTemplate<String, Quote> kafkaTemplate;

    public void publish(Quote quote) {
        log.debug("Sending quote '{}' to topic '{}'", quote.toString(), topic);
        kafkaTemplate.send(topic, quote);
    }
}