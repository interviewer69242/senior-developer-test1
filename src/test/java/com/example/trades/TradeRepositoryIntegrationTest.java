package com.example.trades;
import org.junit.jupiter.api.Test;
import org.flywaydb.core.Flyway;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
@Testcontainers(disabledWithoutDocker=true)
class TradeRepositoryIntegrationTest {
    @Container static PostgreSQLContainer<?> db = new PostgreSQLContainer<>("postgres:16");
    @Test void persistsAndReadsTrade() {
        var ds=new DriverManagerDataSource(db.getJdbcUrl(),db.getUsername(),db.getPassword());
        Flyway.configure().dataSource(ds).load().migrate();
        var repo=new TradeRepository(new JdbcTemplate(ds));
        var trade=new Trade(UUID.randomUUID(),new TradeRequest("client-a","order-1","ABC",TradeRequest.Side.BUY,new BigDecimal("10"),new BigDecimal("12.50")),Instant.now());
        repo.insert(trade);
        var saved=repo.findById(trade.id()).orElseThrow();
        assertThat(saved.id()).isEqualTo(trade.id());
        assertThat(saved.request().clientTradeId()).isEqualTo("order-1");
        assertThat(saved.request().price()).isEqualByComparingTo("12.50");
    }
}
