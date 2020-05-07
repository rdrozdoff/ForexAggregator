package org.home.forex.utils;

import org.apache.commons.math3.util.Precision;
import org.home.forex.model.Quote;

import java.time.Instant;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.DoubleStream;

public class QuoteGenerator {

    private final String symbol;

    private final double basePrice;

    private final AtomicLong seed;

    private final Random random;

    public QuoteGenerator(String symbol, double basePrice, long seed) {
        this.symbol = symbol;
        this.basePrice = basePrice;
        this.seed = new AtomicLong(seed);
        this.random = new Random();
    }

    public Quote nextValue() {
        long next = seed.getAndIncrement();
        double nextPrice = basePrice + Math.log(next) * Math.sin(next) * Math.sin(next);

        double price = generatePrice(nextPrice, 0.05);
        double open = generatePrice(price, 0.01);
        double close = generatePrice(price, 0.01);
        double high = generatePrice(price, 0.01);
        double low = generatePrice(price, 0.01);
        high = DoubleStream.of(open, close, high, low).max().getAsDouble();
        low = DoubleStream.of(open, close, high, low).min().getAsDouble();

        return new Quote(symbol, Instant.now().toEpochMilli(), open, close, high, low);
    }

    private double generatePrice(double basePrice, double deviation) {
        return Precision.round(basePrice + deviation * random.nextDouble(), 5);
    }
}
