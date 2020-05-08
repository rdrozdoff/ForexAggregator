package org.home.forex.strategy;

import lombok.extern.slf4j.Slf4j;
import org.home.forex.model.Quote;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Qualifier("simpleAggregationStrategy")
@Slf4j
@Component
public class AggregationStrategyImpl implements AggregationStrategy {

    public Quote aggregate(Quote quote, Quote aggregatedQuote) {
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
}
