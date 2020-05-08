package org.home.forex.controller;

import org.home.forex.model.Quote;
import org.home.forex.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
public class DataController {

    public static int DEFAULT_PAGE_SIZE = 100;

    @Autowired
    private QuoteService quoteService;

    @GetMapping(value = "/quotes")
    public ResponseEntity<List<Quote>> quotes(@RequestParam(required = false) @Min(1) @Max(500) Integer limit) {
        return ResponseEntity.ok(quoteService.getLastQuotes(limit != null ? limit : DEFAULT_PAGE_SIZE));
    }
}
