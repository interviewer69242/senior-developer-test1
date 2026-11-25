package com.example.trades;
import java.util.UUID;
import java.time.Instant;
public record Trade(UUID id, TradeRequest request, Instant createdAt) {}
