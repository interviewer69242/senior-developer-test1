package com.example.trades;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestClient;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

@Component
public class SettlementClient {
    private final RestClient client;
    private final ObjectMapper mapper;

    public SettlementClient(@Value("${settlement.url}") String url, ObjectMapper mapper) {
        this.mapper = mapper;
        var factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(1000);
        factory.setReadTimeout(2000);
        client = RestClient.builder().baseUrl(url).requestFactory(factory).build();
    }

    public void settle(Trade trade) {
        // The local simulator expects a fixed-length JSON request body.
        final byte[] body;
        try {
            body = mapper.writeValueAsBytes(trade);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Cannot serialize trade for settlement", e);
        }
        client.post().uri("/settlements")
            .contentType(MediaType.APPLICATION_JSON)
            .contentLength(body.length)
            .body(body)
            .retrieve().toBodilessEntity();
    }
}
