#!/usr/bin/env bash
set -euo pipefail
base="${BASE_URL:-http://localhost:8080}"
wait_for() {
  local url="$1"
  for ((i=0;i<60;i++)); do
    if curl -fsS --max-time 3 "$url" >/dev/null 2>&1; then return 0; fi
    sleep 2
  done
  echo "Timed out waiting for $url" >&2
  return 1
}
wait_for http://localhost:8081/actuator/health
wait_for "$base/"
wait_for http://localhost:9090/-/ready
wait_for http://localhost:3000/api/health
ref="smoke-$(date +%s)-$$"
body=$(curl -fsS -X POST "$base/trades" -H 'Content-Type: application/json' -d "{\"clientId\":\"smoke\",\"clientTradeId\":\"$ref\",\"instrument\":\"ABC\",\"side\":\"BUY\",\"quantity\":10,\"price\":12.50}")
id=$(printf '%s' "$body" | python3 -c 'import json,sys; print(json.load(sys.stdin)["id"])')
curl -fsS "$base/trades/$id" >/dev/null
curl -fsS http://localhost:8081/actuator/prometheus >/dev/null
printf 'Smoke test passed; created %s\n' "$id"
