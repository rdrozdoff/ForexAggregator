package org.home.forex;

import org.home.forex.model.Quote;
import org.home.forex.service.QuoteService;
import org.home.forex.test.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import({QuoteService.class, QuoteConsumer.class})
public class QuoteConsumerTest {

    @Autowired
    private QuoteConsumer quoteConsumer;

    @Autowired
    private QuoteService quoteService;

    @Test
    public void shouldReceiveAndStoreQuote() {
        Quote sampleQuote = TestUtils.createQuote();
        quoteConsumer.consume(sampleQuote);
        List<Quote> storedQuotes = quoteService.getLastQuotes(10);

        assertEquals(1, storedQuotes.size());
        assertEquals(sampleQuote, storedQuotes.get(0));
    }
}
