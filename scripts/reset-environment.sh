#!/usr/bin/env bash
set -euo pipefail
if [[ "${1:-}" != "--confirm" ]]; then
  echo "Deletes local exercise database volumes. Run with --confirm."
  exit 2
fi
docker compose down -v
docker compose up -d --build
bash scripts/smoke-test.sh
