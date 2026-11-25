package com.example.trades;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.time.OffsetDateTime;
@Repository
public class TradeRepository {
    private final JdbcTemplate jdbc;
    public TradeRepository(JdbcTemplate jdbc) { this.jdbc = jdbc; }
    public void insert(Trade trade) {
        var r = trade.request();
        jdbc.update("INSERT INTO trades(id,client_id,client_trade_id,instrument,side,quantity,price,created_at) VALUES (?,?,?,?,?,?,?,?)",
            trade.id(),r.clientId(),r.clientTradeId(),r.instrument(),r.side().name(),r.quantity(),r.price(),trade.createdAt().atOffset(java.time.ZoneOffset.UTC));
    }
    public Optional<Trade> findById(UUID id) {
        return jdbc.query("SELECT * FROM trades WHERE id=?", (rs,n) -> new Trade(
            rs.getObject("id",UUID.class),new TradeRequest(rs.getString("client_id"),rs.getString("client_trade_id"),rs.getString("instrument"),TradeRequest.Side.valueOf(rs.getString("side")),rs.getBigDecimal("quantity"),rs.getBigDecimal("price")),rs.getObject("created_at",OffsetDateTime.class).toInstant()),id).stream().findFirst();
    }
}
