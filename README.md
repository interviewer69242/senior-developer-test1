# Trade Operations Portal | Round 2

Senior software engineer practical assessment. Synthetic data only. The exercise has a working starter architecture, but intentionally incomplete business behaviour. Do not use real credentials or proprietary systems.

## Setup

Docker with Compose, Bash, curl and Python 3 are sufficient for the smoke test. From a clean clone:

```bash
docker compose up -d --build
bash scripts/smoke-test.sh
```

| Service | Local URL |
|---|---|
| React portal and proxied REST API | http://localhost:8080 |
| Direct backend and legacy test console | http://localhost:8081 |
| Settlement simulator | http://localhost:8090/settlements |
| Prometheus | http://localhost:9090 |
| Grafana | http://localhost:3000/d/trades-overview |
| Health | http://localhost:8081/actuator/health |

Ports bind only to loopback. The simulator and anonymous Grafana viewer are for local exercises, not production.

The primary build is now Gradle (8.12.1, JDK 21). For local backend development run `gradle test` and `gradle bootRun` after starting db and settlement. Stop the Compose app first to avoid port conflicts or set SERVER_PORT=8081. PostgreSQL integration tests require Docker. For frontend development use Node 22, `cd frontend && npm install && npm run dev` (Vite forwards /trades to the backend on 8081); `npm test` and `npm run build` validate frontend code. When running Gradle directly with the Vite proxy, set SERVER_PORT=8081. Maven is no longer supported.

## API

POST /trades accepts JSON; successful first submission returns 201, a trade UUID, request, creation timestamp and Location. GET /trades/{id} retrieves a trade or returns 404. Invalid input returns 400.

```bash
curl -i http://localhost:8080/trades -H 'Content-Type: application/json' -d '{"clientId":"client-a","clientTradeId":"order-1","instrument":"ABC","side":"BUY","quantity":10,"price":12.50}'
```

## Interview tasks (90 minutes total)

1. **Design (10 min):** Clarify requirements and explain the approach before using AI.
2. **Backend (30 min):** Prevent duplicate trade records on client retries, including concurrent requests across multiple instances. Preserve the existing first-submission contract and validation. References are scoped to a client. Handle conflicting payloads explicitly and document response semantics. Avoid duplicate settlement calls on ordinary successful replays. Add meaningful automated tests, ideally including a real database concurrency test. Discuss migration safety and downstream uncertain outcomes; a complete distributed redesign is not required.
3. **Frontend (10 min):** In the React portal, prevent duplicate clicks while the request is pending, distinguish new submissions, successful replays, conflicts and failures, and preserve the original payload/reference for retries. Show meaningful feedback and add focused tests where possible.
4. **Incident investigation (15 min):** The interviewer will provide an incident report. Use available logs, metrics and commands to diagnose the fault and propose safe mitigation. Start independently.
5. **Delivery review (10 min):** Review infrastructure/pipeline-review.yaml and infrastructure/kubernetes/app.yaml; explain production risks and prioritized improvements. The pipeline sample is not executable. The Kubernetes file is illustrative, requires an available image and separately supplied database/settlement services and secret; no cluster is required.
6. **Technical defence (10 min):** Explain changes, trade-offs, verification and outstanding risks. An initial five-minute environment walkthrough completes the schedule.

AI and documentation are permitted during implementation. Be prepared to explain and verify all changes; do not disable tests or validation. Prioritize correctness over completeness. Do not change infrastructure just to hide failures.

## Operations

Use `docker compose ps`, `docker compose logs --tail=100 app frontend settlement db`, Grafana and the health/metrics endpoints to investigate. The simulator has normal and fault-injection modes; the interviewer will control them. To rebuild changes: `docker compose up -d --build`. To clear exercise data between candidates (destructive): `bash scripts/reset-environment.sh --confirm`.

## Deliverables

Focused source changes, tests, a short explanation of assumptions and remaining risks, and evidence of verification. The repository intentionally does not contain the interviewer answer key or confidential evaluation materials.
