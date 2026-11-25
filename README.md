# Trade Processing Service

Senior backend developer interview exercise (30-minute implementation, followed by review). All data is synthetic. AI coding agents and documentation are allowed during implementation; be prepared to explain every change. Do not use real credentials or external trading systems.

## Run locally

Docker with Compose is sufficient: `docker compose up --build -d` then `docker compose logs -f app` until Spring reports startup. Alternatively use Java 21 and Maven 3.9: `docker compose up -d db settlement`, then `mvn spring-boot:run`. Run baseline unit tests with `mvn test`. Clean up with `docker compose down -v` (deletes exercise data).

## Existing API

POST /trades returns HTTP 201 with a generated UUID, request payload, creation timestamp and Location header. GET /trades/{id} returns the persisted trade or 404. Invalid submissions return 400.

```sh
curl -i http://localhost:8080/trades -H 'Content-Type: application/json' -d '{"clientId":"client-a","clientTradeId":"order-1","instrument":"ABC","side":"BUY","quantity":10,"price":12.50}'
curl http://localhost:8090/settlements
```

The settlement simulator exposes GET /settlements for inspection, DELETE /settlements to reset its in-memory records, and POST /mode with `{"mode":"normal"}`, `{"mode":"fail"}` or `{"mode":"timeout_after_accept"}` for fault injection. It is deliberately non-idempotent and is not a real settlement system.

## Assignment

Clients may retry trade submission when a network timeout occurs. Modify the existing service so repeated submissions of the same trade do not create duplicate trade records. Concurrent submissions, including requests handled by different service instances, must be safe. Preserve the existing request shape and successful first-submission behaviour. A client trade reference is scoped to its client; different clients may use the same reference. Discuss how conflicting payloads for the same reference should be handled, and document the chosen response semantics. Avoid duplicate settlement calls for ordinary successful replays. Discuss downstream failure and uncertain outcomes; a complete distributed settlement redesign is not required in the timebox.

Add meaningful automated tests, including retry and concurrency behaviour where possible. Preserve existing data safely when changing the schema; explain how you would handle pre-existing duplicates. Keep the changes focused. Do not disable existing validation or tests.

## Deliverables

A working change, automated tests, and a short explanation of assumptions, verification performed, remaining limitations and production considerations. If time runs out, explain what remains and prioritize correctness over completeness. You may use AI freely during the implementation stage; the interviewer will ask you to defend the output.
