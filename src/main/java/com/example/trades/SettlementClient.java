package com.example.trades;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestClient;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
@Component
public class SettlementClient {
    private final RestClient client;
    public SettlementClient(@Value("${settlement.url}") String url) {
        var factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(1000); factory.setReadTimeout(2000);
        client = RestClient.builder().baseUrl(url).requestFactory(factory).build();
    }
    public void settle(Trade trade) {
        client.post().uri("/settlements").body(trade).retrieve().toBodilessEntity();
    }
}
