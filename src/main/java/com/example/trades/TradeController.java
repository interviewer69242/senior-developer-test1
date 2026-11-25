package com.example.trades;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.UUID;
@RestController
@RequestMapping("/trades")
public class TradeController {
    private final TradeService service;
    private final TradeRepository repository;
    public TradeController(TradeService service, TradeRepository repository) { this.service=service; this.repository=repository; }
    @PostMapping
    public ResponseEntity<Trade> submit(@Valid @RequestBody TradeRequest request) {
        var trade = service.submit(request);
        return ResponseEntity.created(URI.create("/trades/"+trade.id())).body(trade);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Trade> get(@PathVariable UUID id) { return ResponseEntity.of(repository.findById(id)); }
}
