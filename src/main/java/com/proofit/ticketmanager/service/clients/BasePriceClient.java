package com.proofit.ticketmanager.service.clients;

import com.proofit.ticketmanager.domain.BasePriceClientResponse;
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
import java.util.Objects;

import static com.proofit.ticketmanager.service.util.Constants.ERROR_GETTING_DATA_FROM_CLIENT;
import static org.slf4j.LoggerFactory.getLogger;

@Component
public class BasePriceClient {

    @Value("${developmentMode}")
    private boolean developmentMode;

    private final WebClient webClient;

    private final Logger logger = getLogger(getClass());

    @Autowired
    public BasePriceClient(@Value("${BASE_PRICE_URL}") String basePriceUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(basePriceUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public BigDecimal getBasePrice(final String destination) {
        logger.info("Attempting to call base price client");
        if (developmentMode) {
            return BigDecimal.valueOf(10); //for testing purposes
        }
        try {
            BasePriceClientResponse response = webClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/base-price")
                            .queryParam("finalDestination", destination)
                            .build())
                    .retrieve()
                    .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse -> Mono.empty())
                    .bodyToMono(BasePriceClientResponse.class)
                    .block();
            return Objects.requireNonNull(response).getBasePrice();
        } catch (Exception e) {
            logger.error("Error retrieving data");
            throw new TicketManagerException(ERROR_GETTING_DATA_FROM_CLIENT, e);
        }
    }
}
