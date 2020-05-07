package org.home.forex.processor;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.WindowedSerdes;
import org.home.forex.model.Quote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
public class QuoteAggregator {

    private static final Logger log = LoggerFactory.getLogger(QuoteAggregator.class);

    @Value("${aggregator.topic.output}")
    private String outputTopic;

    @Value("${aggregator.period}")
    private Long aggregationPeriod;

    public void process(KStream<String, Quote> stream){
        Duration windowDuration = Duration.ofMillis(aggregationPeriod);
        stream
            .map((k, v) -> new KeyValue<>(groupTimestamp(v.getTimestamp(), windowDuration), v))
            .groupByKey()
            .windowedBy(TimeWindows.of(windowDuration))
            .aggregate(this::initialize, this::aggregateQuotes)
            .toStream()
            .to(outputTopic, Produced.keySerde(WindowedSerdes.timeWindowedSerdeFrom(Long.class)));
    }

    private Quote initialize() {
        log.debug("Starting aggregation");
        return new Quote(null, Instant.now().toEpochMilli(),null,null, Double.MIN_VALUE, Double.MAX_VALUE);
    }

    private Quote aggregateQuotes(Long timestamp, Quote quote, Quote aggregatedQuote) {
        aggregatedQuote.setSymbol(quote.getSymbol());
        aggregatedQuote.setTimestamp(quote.getTimestamp());

        if (aggregatedQuote.getOpen() == null) {
            aggregatedQuote.setOpen(quote.getOpen());
        }
        aggregatedQuote.setClose(quote.getClose());
        aggregatedQuote.setHigh(Math.max(quote.getHigh(), aggregatedQuote.getHigh()));
        aggregatedQuote.setLow(Math.min(quote.getLow(), aggregatedQuote.getLow()));

        log.debug("Aggregating quote '{}'", quote);

        return aggregatedQuote;
    }

    private Long groupTimestamp(Long timestamp, Duration duration) {
        return timestamp / duration.toMillis();
    }
}