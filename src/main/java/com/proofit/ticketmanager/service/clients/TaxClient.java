package com.proofit.ticketmanager.service.clients;

import com.proofit.ticketmanager.domain.TaxClientResponse;
import com.proofit.ticketmanager.exception.TicketManagerException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static com.proofit.ticketmanager.service.util.Constants.ERROR_GETTING_DATA_FROM_CLIENT;
import static java.util.Objects.requireNonNull;
import static org.slf4j.LoggerFactory.getLogger;

@Component
public class TaxClient {

    @Value("${developmentMode}")
    private boolean developmentMode;

    private final WebClient webClient;

    private final Logger logger = getLogger(getClass());

    @Autowired
    public TaxClient(@Value("${TAX_URL}") String taxUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(taxUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public BigDecimal getTax() {
        logger.info("Attempting to call tax client");
        if (developmentMode) {
            return BigDecimal.valueOf(0.21); //for testing purposes
        }
        try {
            TaxClientResponse response = webClient.get()
                    .uri("/tax")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .retrieve()
                    .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse -> Mono.empty())
                    .bodyToMono(TaxClientResponse.class)
                    .block();
            return requireNonNull(response).getTax();
        } catch (Exception e) {
            logger.error("Error retrieving data");
            throw new TicketManagerException(ERROR_GETTING_DATA_FROM_CLIENT, e);
        }
    }
}
