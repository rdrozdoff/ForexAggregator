package org.home.forex.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.home.forex.model.Quote;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ContextConfiguration(classes = {DataController.class})
@WebMvcTest
public class DataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnNonEmptyQuote() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/quotes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        assertTrue(StringUtils.isNotBlank(responseBody));
        Quote quote = new ObjectMapper().readValue(responseBody, Quote.class);

        assertNotNull(quote);
        assertNotNull(quote.getSymbol());
        assertNotNull(quote.getTimestamp());
        assertNotNull(quote.getOpen());
        assertNotNull(quote.getClose());
        assertNotNull(quote.getHigh());
        assertNotNull(quote.getLow());
    }
}