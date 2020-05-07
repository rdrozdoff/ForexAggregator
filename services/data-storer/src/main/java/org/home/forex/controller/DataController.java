package org.home.forex.controller;

import org.home.forex.model.Quote;
import org.home.forex.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DataController {

    @Autowired
    private QuoteService quoteService;

    @GetMapping(value = "/quotes")
    public ResponseEntity<List<Quote>> quotes() {
        return ResponseEntity.ok(quoteService.findAll());
    }
}
