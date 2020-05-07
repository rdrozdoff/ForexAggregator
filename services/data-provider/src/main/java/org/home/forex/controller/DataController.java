package org.home.forex.controller;

import org.apache.commons.math3.util.Precision;
import org.home.forex.model.Quote;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Random;
import java.util.stream.DoubleStream;

@RestController
public class DataController {

    private final Random random = new Random();

    @Value("${feed.basePrice}")
    private Double basePrice;

    @GetMapping(value = "/quotes")
    public ResponseEntity<Quote> quotes(@RequestParam(value = "symbol", defaultValue = "EURUSD") String symbol) {
        double open = generatePrice(basePrice);
        double close = generatePrice(basePrice);
        double high = generatePrice(basePrice);
        double low = generatePrice(basePrice);
        high = DoubleStream.of(open, close, high, low).max().getAsDouble();
        low = DoubleStream.of(open, close, high, low).min().getAsDouble();

        return ResponseEntity.ok(new Quote(symbol, Instant.now().toEpochMilli(), open, close, high, low));
    }

    private double generatePrice(double basePrice) {
        return Precision.round(basePrice + random.nextDouble() / 100, 5);
    }
}
