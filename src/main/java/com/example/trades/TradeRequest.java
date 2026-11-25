package com.example.trades;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
public record TradeRequest(@NotBlank @Size(max=80) String clientId,
    @NotBlank @Size(max=80) String clientTradeId,
    @NotBlank @Size(max=20) String instrument,
    @NotNull Side side,
    @NotNull @Positive @Digits(integer=12,fraction=4) BigDecimal quantity,
    @NotNull @Positive @Digits(integer=12,fraction=4) BigDecimal price) {
    public enum Side { BUY, SELL }
}
