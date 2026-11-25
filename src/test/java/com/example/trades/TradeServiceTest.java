package com.example.trades;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
class TradeServiceTest {
    @Test void submitsTradeAndCallsSettlement() {
        var repository=mock(TradeRepository.class);
        var settlement=mock(SettlementClient.class);
        var service=new TradeService(repository,settlement);
        var request=new TradeRequest("client-a","order-1","ABC",TradeRequest.Side.BUY,new BigDecimal("10"),new BigDecimal("12.50"));
        var trade=service.submit(request);
        assertThat(trade.id()).isNotNull();
        assertThat(trade.request()).isEqualTo(request);
        var order=inOrder(repository,settlement);
        order.verify(repository).insert(trade);
        order.verify(settlement).settle(trade);
    }
}
