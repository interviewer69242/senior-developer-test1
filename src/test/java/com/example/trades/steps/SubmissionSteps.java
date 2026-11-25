package com.example.trades.steps;
import com.example.trades.*;
import io.cucumber.java.en.*;
import java.math.BigDecimal;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
public class SubmissionSteps {
    private TradeRepository repository;
    private SettlementClient settlement;
    private TradeService service;
    private TradeRequest request;
    private Trade trade;
    @Given("a valid trade request") public void givenRequest() {
        repository=mock(TradeRepository.class); settlement=mock(SettlementClient.class);
        service=new TradeService(repository,settlement);
        request=new TradeRequest("client-a","order-1","ABC",TradeRequest.Side.BUY,new BigDecimal("10"),new BigDecimal("12.50"));
    }
    @When("the trade service submits it") public void submit(){trade=service.submit(request);}
    @Then("a trade identifier is returned") public void identifier(){assertThat(trade.id()).isNotNull();}
    @And("settlement receives the trade") public void settle(){verify(settlement).settle(trade);}
}
