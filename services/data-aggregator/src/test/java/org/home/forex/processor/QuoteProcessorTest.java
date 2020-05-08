package org.home.forex.processor;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.KStream;
import org.home.forex.KafkaStreamBuilder;
import org.home.forex.model.Quote;
import org.home.forex.serialization.QuoteSerde;
import org.home.forex.strategy.AggregationStrategyImpl;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Properties;

public class QuoteProcessorTest {

    private final static String INPUT_TOPIC = "quotes";
    private final static String OUTPUT_TOPIC = "aggregated-quotes";
    private final static Quote sampleQuote =
            new Quote("EURUSD", Instant.now().toEpochMilli(),1.0,2.0,3.0,0.0);

    private TopologyTestDriver testDriver;

    private TestInputTopic<String, Quote> inputTopic;

    private TestOutputTopic<String, Quote> outputTopic;

    @BeforeEach
    public void setUp() {
        QuoteProcessor quoteProcessor = new QuoteProcessor(new AggregationStrategyImpl(), OUTPUT_TOPIC, 5000L);
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        KStream<String, Quote> stream = new KafkaStreamBuilder(INPUT_TOPIC, quoteProcessor)
                .kafkaStream(streamsBuilder);
        quoteProcessor.createStream(stream);

        Properties streamsConfig = new Properties();
        streamsConfig.put(StreamsConfig.APPLICATION_ID_CONFIG, "aggregator");
        streamsConfig.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        streamsConfig.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.Long().getClass());
        streamsConfig.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, QuoteSerde.class);

        testDriver = new TopologyTestDriver(streamsBuilder.build(), streamsConfig);
        inputTopic = testDriver.createInputTopic(INPUT_TOPIC,
                Serdes.String().serializer(), new QuoteSerde().serializer());
        outputTopic = testDriver.createOutputTopic(OUTPUT_TOPIC,
                Serdes.String().deserializer(), new QuoteSerde().deserializer());
    }

    @Test
    public void shouldAggregateQuotes() {
        inputTopic.pipeInput(sampleQuote);
        Assert.assertFalse(outputTopic.isEmpty());
        Assert.assertEquals(outputTopic.readValue(), sampleQuote);
    }

    @AfterEach
    public void tearDown() {
        testDriver.close();
    }
}
