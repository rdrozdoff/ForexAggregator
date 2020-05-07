package org.home.forex.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Lists;
import org.home.forex.model.Quote;
import org.home.forex.service.QuoteService;
import org.home.forex.test.TestUtils;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.lang.reflect.Type;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ContextConfiguration(classes = {DataController.class})
@WebMvcTest
public class DataControllerTest {

    private final static Quote sampleQuote = TestUtils.createQuote();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuoteService quoteServiceMock;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        Mockito.when(quoteServiceMock.getLastQuotes(DataController.DEFAULT_PAGE_SIZE))
                .thenReturn(Lists.newArrayList(sampleQuote));
    }

    @Test
    public void shouldReturnNonEmptyQuoteList() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/quotes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        assertTrue(StringUtils.isNotBlank(responseBody));

        JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, Quote.class);
        List<Quote> quoteList = new ObjectMapper().readValue(responseBody, type);

        assertEquals(1, quoteList.size());
        assertEquals(sampleQuote, quoteList.get(0));
    }
}