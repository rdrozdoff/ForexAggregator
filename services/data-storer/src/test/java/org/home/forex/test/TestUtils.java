package org.home.forex.test;

import org.home.forex.model.Quote;

import java.time.Instant;

public class TestUtils {

    public static Quote createQuote() {
        return new Quote("EURUSD", Instant.now().toEpochMilli(), 1.0, 2.0, 3.0, 0.0);
    }
}