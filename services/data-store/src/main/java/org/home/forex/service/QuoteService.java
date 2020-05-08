package org.home.forex.service;

import lombok.extern.slf4j.Slf4j;
import org.home.forex.entity.QuoteEntity;
import org.home.forex.model.Quote;
import org.home.forex.repository.QuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class QuoteService {

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

    public List<Quote> getLastQuotes(int pageSize) {
        int lastPage = Math.max(0, (int) quoteRepository.count() / pageSize - 1);
        PageRequest pageRequest = PageRequest.of(lastPage, pageSize);

        return quoteRepository.findAll(pageRequest).getContent().stream()
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
