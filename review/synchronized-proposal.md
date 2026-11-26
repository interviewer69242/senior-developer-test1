# Proposed AI patch for review

A colleague proposes adding the following sketch to the current service. It is a review exercise, not a complete or automatically applicable patch. The lookup method and replay handling are illustrative.

```java
@Transactional
public synchronized Trade submit(TradeRequest request) {
    var existing = repository.findByClientAndReference(
        request.clientId(), request.clientTradeId());
    if (existing.isPresent()) {
        return existing.get();
    }
    var trade = new Trade(UUID.randomUUID(), request, Instant.now());
    repository.insert(trade);
    settlement.settle(trade);
    return trade;
}
```

Explain whether you would accept it, what assumptions it makes, what happens when requests arrive at different instances, how differing payloads are handled, and how you would verify the behaviour. You do not need to apply it.
