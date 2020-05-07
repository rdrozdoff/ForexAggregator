package org.home.forex.service;

import org.home.forex.entity.QuoteEntity;
import org.home.forex.model.Quote;
import org.home.forex.repository.QuoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class QuoteService {

    private static final Logger log = LoggerFactory.getLogger(QuoteService.class);

    @Autowired
    private QuoteRepository quoteRepository;

    public void saveQuote(Quote quote) {
        QuoteEntity quoteEntity = new QuoteEntity();
        quoteEntity.setSymbol(quote.getSymbol());
        quoteEntity.setTimestamp(quote.getTimestamp());
        quoteEntity.setOpen(quote.getOpen());
        quoteEntity.setClose(quote.getClose());
        quoteEntity.setHigh(quote.getHigh());
        quoteEntity.setLow(quote.getLow());

        log.debug("Saving quote '{}'", quote);
        quoteRepository.save(quoteEntity);
    }

    public List<Quote> findAll() {
        return StreamSupport.stream(quoteRepository.findAll().spliterator(), false)
                .map(quoteEntity -> {
                    log.debug("Loading quote '{}'", quoteEntity);

                    Quote quote = new Quote();
                    quote.setSymbol(quoteEntity.getSymbol());
                    quote.setTimestamp(quoteEntity.getTimestamp());
                    quote.setOpen(quoteEntity.getOpen());
                    quote.setClose(quoteEntity.getClose());
                    quote.setHigh(quoteEntity.getHigh());
                    quote.setLow(quoteEntity.getLow());

                    return quote;
                }).collect(Collectors.toList());
    }
}
