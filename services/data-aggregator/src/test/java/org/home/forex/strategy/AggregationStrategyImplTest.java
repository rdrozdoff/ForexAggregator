package org.home.forex.strategy;

import org.assertj.core.util.Lists;
import org.home.forex.model.Quote;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class AggregationStrategyImplTest {

    private AggregationStrategyImpl aggregationStrategy = new AggregationStrategyImpl();

    @Test
    public void shouldAggregateQuotes() {
        List<Quote> sampleQuotes = Lists.newArrayList(
                new Quote("EURUSD", 1L,1.0,2.0,2.5,1.0),
                new Quote("EURUSD", 2L,1.1,2.1,3.0,1.0),
                new Quote("EURUSD", 3L,1.2,2.2,3.5,2.0)
        );

        Quote aggregatedQuote = new Quote(null, 0L, null, null, Double.MIN_VALUE, Double.MAX_VALUE);
        for (Quote quote : sampleQuotes) {
            aggregatedQuote = aggregationStrategy.aggregate(quote, aggregatedQuote);
        }

        assertEquals(new Quote("EURUSD", 3L, 1.0, 2.2, 3.5, 1.0), aggregatedQuote);
    }
}
