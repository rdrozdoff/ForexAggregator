package org.home.forex;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.home.forex.model.Quote;
import org.home.forex.processor.QuoteAggregator;
import org.home.forex.serialization.QuoteSerde;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@Configuration
@EnableKafka
@EnableKafkaStreams
public class KafkaConfig {

    @Value("${aggregator.topic.input}")
    private String inputTopic;

    @Autowired
    private QuoteAggregator quoteAggregator;

    @Bean
    public KStream<String, Quote> kafkaStream(StreamsBuilder streamsBuilder) {
        KStream<String, Quote> stream = streamsBuilder
                .stream(inputTopic, Consumed.with(Serdes.String(), new QuoteSerde()));

        this.quoteAggregator.process(stream);

        return stream;
    }
}