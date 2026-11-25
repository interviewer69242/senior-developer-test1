package com.example.trades;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.UUID;
@Service
public class TradeService {
    private final TradeRepository repository;
    private final SettlementClient settlement;
    public TradeService(TradeRepository repository, SettlementClient settlement) { this.repository=repository; this.settlement=settlement; }
    @Transactional
    public Trade submit(TradeRequest request) {
        var trade = new Trade(UUID.randomUUID(), request, Instant.now());
        repository.insert(trade);
        settlement.settle(trade);
        return trade;
    }
}
