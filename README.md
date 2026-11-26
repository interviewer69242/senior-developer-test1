# Trade Operations Portal - 120-minute assessment

This synthetic Java 21/Spring Boot, PostgreSQL and React application records trades and calls a local settlement simulator. It contains a known correctness defect. Work in an isolated environment using synthetic data only. The persistence implementation uses Spring JDBC; inspect the code before assuming an ORM.

## Run the application

Requirements: Docker with Compose, Bash, curl and Python 3. From the repository root:

```bash
docker compose up -d --build
bash scripts/smoke-test.sh
```

Portal: http://localhost:8080; direct backend: http://localhost:8081; settlement records: http://localhost:8090/settlements; health: http://localhost:8081/actuator/health; Prometheus: http://localhost:9090; Grafana: http://localhost:3000/d/trades-overview. Stop with `docker compose down`. The interviewer provides a prepared environment if required.

The backend uses Gradle 8.12.1 and Java 21. Run `gradle test` locally; database integration tests require Docker. For the frontend, use Node 22: `cd frontend && npm install && npm test && npm run build`. See `scripts/test-backend.sh` for a container-based option.

## Current API

POST /trades accepts a JSON request. A successful new trade returns 201 with a Location header and trade body; invalid input returns 400. GET /trades/{id} returns a trade or 404.

```bash
curl -i http://localhost:8080/trades -H 'Content-Type: application/json' -d '{"clientId":"client-a","clientTradeId":"order-1","instrument":"ABC","side":"BUY","quantity":10,"price":12.50}'
```

## Scenario

Clients retry when requests time out. Duplicate submissions currently create duplicate trades. Implement an idempotent API that remains correct across concurrent requests and service instances:

- Identity is the pair (clientId, clientTradeId). An initial valid submission returns 201 and the existing response shape and Location.
- An identical replay returns 200 and the original trade; a reuse of the same identity with different business fields returns 409. Invalid input remains 400. Different clients may reuse a reference.
- Avoid duplicate settlement calls on ordinary successful replays. Explain any uncertainty around a timeout or crash between database and external settlement; a full distributed redesign is not expected.
- Include meaningful tests, including database-backed concurrency coverage where possible. Explain any missing verification. Consider existing data when proposing a migration.

## Interview plan

| Time | Activity | AI |
|---|---|---|
| 00-10 | Inspect, clarify, and outline approach | Off |
| 10-40 | Investigate and implement | Allowed |
| 40-55 | Review `review/synchronized-proposal.md` and explain its risks | Off |
| 55-75 | Add and run tests; explain evidence | Allowed |
| 75-90 | Review pipeline and Kubernetes readiness | Off |
| 90-95 | Discuss a new event-publishing requirement | Off |
| 95-110 | Implement, test or outline a safe response to that change | Allowed |
| 110-120 | Defend decisions, gaps and risks | Off |

Your interviewer will introduce the event requirement at the stated time. Manual work is equally acceptable; AI is optional. Be ready to explain every change and verify generated code. You may use documentation. Do not use real data or secrets. If setup fails, tell the interviewer so the clock can be paused.

## Repository map and deliverables

`src/main/java` contains the API, service, persistence and settlement client; `src/test` contains starter tests; `frontend` is the portal; `infrastructure/pipeline-review.yaml` is an illustrative pipeline and `infrastructure/kubernetes/app.yaml` is a deployment example. `simulator` implements the local downstream.

Deliver a focused change, tests and a short explanation of assumptions, commands run, results and limitations. Explain how the frontend and delivery process would adapt to new response semantics. No Kubernetes cluster is required.
