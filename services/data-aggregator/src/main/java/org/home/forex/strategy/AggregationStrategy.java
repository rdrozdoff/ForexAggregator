package org.home.forex.strategy;

import org.home.forex.model.Quote;

public interface AggregationStrategy {

    Quote aggregate(Quote quote, Quote aggregatedQuote);

}
