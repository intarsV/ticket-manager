package com.proofit.ticketmanager.service.clients;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaxClientTest {

    private final MockWebServer mockWebServer = new MockWebServer();
    private final TaxClient taxClient = new TaxClient(mockWebServer.url("localhost/").toString());

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void shouldReturnPost() {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody("{\"tax\": 0.21}"));

        final BigDecimal response = taxClient.getTax();
        assertEquals(BigDecimal.valueOf(0.21), response);
    }
}