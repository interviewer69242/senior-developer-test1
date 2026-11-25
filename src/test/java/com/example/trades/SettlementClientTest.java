package com.example.trades;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.Test;
import java.net.InetSocketAddress;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import static org.assertj.core.api.Assertions.assertThat;

class SettlementClientTest {
    @Test void sendsFixedLengthJsonToSettlement() throws Exception {
        var server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        var received = new AtomicReference<byte[]>();
        var length = new AtomicReference<String>();
        var type = new AtomicReference<String>();
        server.createContext("/settlements", exchange -> {
            try {
                length.set(exchange.getRequestHeaders().getFirst("Content-Length"));
                type.set(exchange.getRequestHeaders().getFirst("Content-Type"));
                if (length.get() == null) {
                    exchange.sendResponseHeaders(400, -1);
                } else {
                    received.set(exchange.getRequestBody().readAllBytes());
                    exchange.sendResponseHeaders(201, -1);
                }
            } finally {
                exchange.close();
            }
        });
        server.start();
        try {
            var mapper = new ObjectMapper().findAndRegisterModules();
            var trade = new Trade(UUID.randomUUID(),
                new TradeRequest("client-a", "order-1", "ABC", TradeRequest.Side.BUY,
                    new BigDecimal("10"), new BigDecimal("12.50")), Instant.now());
            var client = new SettlementClient("http://127.0.0.1:" + server.getAddress().getPort(), mapper);
            client.settle(trade);
            assertThat(length.get()).isEqualTo(String.valueOf(received.get().length));
            assertThat(type.get()).startsWith("application/json");
            assertThat(mapper.readTree(received.get())).isEqualTo(mapper.readTree(mapper.writeValueAsBytes(trade)));
        } finally {
            server.stop(0);
        }
    }
}
