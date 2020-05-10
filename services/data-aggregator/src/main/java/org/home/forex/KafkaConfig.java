package org.home.forex;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.home.forex.model.Quote;
import org.home.forex.processor.QuoteProcessor;
import org.home.forex.serialization.QuoteSerde;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@Configuration
@EnableKafka
@EnableKafkaStreams
public class KafkaConfig {

    private String inputTopic;

    private QuoteProcessor quoteProcessor;

    public KafkaConfig(@Value("${aggregator.topic.input}") String inputTopic, QuoteProcessor quoteProcessor) {
        this.inputTopic = inputTopic;
        this.quoteProcessor = quoteProcessor;
    }

    @Bean
    public KStream<String, Quote> kafkaStream(StreamsBuilder streamsBuilder) {
        KStream<String, Quote> stream = streamsBuilder
                .stream(inputTopic, Consumed.with(Serdes.String(), new QuoteSerde()));

        this.quoteProcessor.createStream(stream);

        return stream;
    }

    @Bean
    public NewTopic inputTopic(@Value("${aggregator.topic.input}") String inputTopic) {
        return new NewTopic(inputTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic outputTopic(@Value("${aggregator.topic.output}") String outputTopic) {
        return new NewTopic(outputTopic, 1, (short) 1);
    }
}