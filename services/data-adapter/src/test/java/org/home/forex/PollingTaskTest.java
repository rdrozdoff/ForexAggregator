package org.home.forex;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.home.forex.model.Quote;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PollingTaskTest {

    private final static Quote sampleQuote = new Quote(
            "EURUSD",
            123456789L,
            1.0,
            2.0,
            3.0,
            0.0
    );

    @MockBean
    private KafkaTemplate<String, Quote> kafkaTemplate;

    private static MockWebServer mockWebServer;

    @BeforeClass
    public static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.enqueue(new MockResponse()
            .setResponseCode(HttpStatus.OK.value())
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(new ObjectMapper().writeValueAsString(sampleQuote))
        );
        mockWebServer.start(50000);
    }

    @Test
    public void shouldRetrieveAndPublishQuote() throws Exception {
        verify(kafkaTemplate, timeout(10000).atLeast(1)).send("quotes", sampleQuote);
    }

    @AfterClass
    public static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }
}
