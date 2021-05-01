package com.proofit.ticketmanager.service.clients;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BasePriceClientTest {

    private final MockWebServer mockWebServer = new MockWebServer();
    private final BasePriceClient basePriceClient = new BasePriceClient(mockWebServer.url("localhost/").toString());

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
                        .setBody("{\"basePrice\": 10}"));

        final BigDecimal response = basePriceClient.getBasePrice("Somewhere");
        assertEquals(BigDecimal.valueOf(10), response);
    }

}