#!/usr/bin/env bash
set -euo pipefail
base="${BASE_URL:-http://localhost:8080}"
ref="smoke-$(date +%s)-$$"
for ((i=0;i<60;i++)); do
  if curl -fsS http://localhost:8081/actuator/health >/dev/null; then break; fi
  sleep 2
done
curl -fsS http://localhost:8081/actuator/health >/dev/null
curl -fsS "$base/" >/dev/null
body=$(curl -fsS -X POST "$base/trades" -H 'Content-Type: application/json' -d "{\"clientId\":\"smoke\",\"clientTradeId\":\"$ref\",\"instrument\":\"ABC\",\"side\":\"BUY\",\"quantity\":10,\"price\":12.50}")
id=$(printf '%s' "$body" | python3 -c 'import json,sys; print(json.load(sys.stdin)["id"])')
curl -fsS "$base/trades/$id" >/dev/null
curl -fsS http://localhost:8081/actuator/prometheus >/dev/null
curl -fsS http://localhost:9090/-/ready >/dev/null
curl -fsS http://localhost:3000/api/health >/dev/null
printf 'Smoke test passed; created %s\n' "$id"
