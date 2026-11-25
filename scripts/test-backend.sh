#!/usr/bin/env bash
set -euo pipefail
# Unit and Cucumber tests; database integration tests need access to Docker.
gradle test
