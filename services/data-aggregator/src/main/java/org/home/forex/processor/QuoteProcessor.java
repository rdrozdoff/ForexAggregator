package org.home.forex.processor;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.WindowedSerdes;
import org.home.forex.model.Quote;
import org.home.forex.strategy.AggregationStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
@Slf4j
public class QuoteProcessor {

    private final AggregationStrategy aggregationStrategy;

    private final Long aggregationPeriod;

    private final String outputTopic;

    public QuoteProcessor(@Qualifier("simpleAggregationStrategy") AggregationStrategy aggregationStrategy,
                          @Value("${aggregator.topic.output}") String outputTopic,
                          @Value("${aggregator.period}") Long aggregationPeriod) {
        this.aggregationStrategy = aggregationStrategy;
        this.aggregationPeriod = aggregationPeriod;
        this.outputTopic = outputTopic;
    }

    public void createStream(KStream<String, Quote> stream){
        Duration windowDuration = Duration.ofMillis(aggregationPeriod);
        stream
            .map((k, v) -> new KeyValue<>(groupTimestamp(v.getTimestamp(), windowDuration), v))
            .groupByKey()
            .windowedBy(TimeWindows.of(windowDuration).grace(Duration.ZERO))
            .aggregate(this::initialize, this::aggregateQuotes)
            .toStream()
            .to(outputTopic, Produced.keySerde(WindowedSerdes.timeWindowedSerdeFrom(Long.class)));
    }

    private Quote initialize() {
        log.debug("Starting aggregation");
        return new Quote(null, Instant.now().toEpochMilli(), null, null, Double.MIN_VALUE, Double.MAX_VALUE);
    }

    private Quote aggregateQuotes(Long timestamp, Quote quote, Quote aggregatedQuote) {
        return aggregationStrategy.aggregate(quote, aggregatedQuote);
    }

    private Long groupTimestamp(Long timestamp, Duration duration) {
        return timestamp / duration.toMillis();
    }
}