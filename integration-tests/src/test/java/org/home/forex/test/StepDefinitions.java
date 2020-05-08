package org.home.forex.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.home.forex.model.Quote;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class StepDefinitions {

    private static MockWebServer mockWebServer;

    private static URI aggregatedQuotesUrl;

    private static ObjectMapper objectMapper = new ObjectMapper();

    private static Quote aggregatedQuote = null;

    @Before
    public void setUp() {
        mockWebServer = new MockWebServer();
    }

    @Given("mock producer is started on port {int}")
    public void startProducerMock(int port) throws IOException {
        mockWebServer.start(port);
    }

    @Given("aggregated quotes are produced at {string}")
    public void configureQuoteStore(String url) throws MalformedURLException {
        aggregatedQuotesUrl = URI.create(url);
    }

    @When("quote is produced in OHLC format [{double}, {double}, {double}, {double}]")
    public void produceQuote(double open, double high, double low, double close) throws Exception {
        Quote quote = new Quote("EURUSD", Instant.now().toEpochMilli(), open, close, high, low);
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.OK.value())
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(objectMapper.writeValueAsString(quote))
        );
    }

    @Then("it should be aggregated in {int} seconds")
    public void shouldBeAggregated(int period) throws Exception {
        aggregatedQuote = null;
        TimeUnit.SECONDS.sleep(period);

        List<Quote> quotes = WebClient.create()
                .get()
                .uri(aggregatedQuotesUrl)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Quote>>() {})
                .block();

        assertTrue("Received empty list of quotes", quotes != null && !quotes.isEmpty());
        aggregatedQuote = quotes.get(quotes.size() - 1);
    }

    @Then("aggregated quote is [{double}, {double}, {double}, {double}]")
    public void quoteEqualTo(double open, double high, double low, double close) {
        Quote expectedQuote = new Quote("EURUSD", Instant.now().toEpochMilli(), open, close, high, low);
        assertNotNull("Quotes are not yet aggregated", aggregatedQuote);
        assertEquals("Quotes are not equal", expectedQuote, aggregatedQuote);
    }

    @After
    public void tearDown() throws IOException {
        mockWebServer.shutdown();
        aggregatedQuote = null;
    }
}
