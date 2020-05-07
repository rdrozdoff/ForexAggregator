package org.home.forex.controller;

import org.home.forex.model.Quote;
import org.home.forex.utils.QuoteGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
public class DataController {

    @Value("${feed.symbol}")
    private String symbol;

    @Value("${feed.basePrice}")
    private Double basePrice;

    private QuoteGenerator quoteGenerator;

    @PostConstruct
    public void init() {
        quoteGenerator = new QuoteGenerator(symbol, basePrice, 1);
    }

    @GetMapping(value = "/quotes")
    public ResponseEntity<Quote> quotes() {
        return ResponseEntity.ok(quoteGenerator.nextValue());
    }
}
