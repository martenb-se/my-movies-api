#!/bin/bash
# ~~~~~~~ Get Environment
source env.load.sh

# ~~~~~~~ Execution
# Run unit tests
docker compose -f docker-compose.testing.yml run --rm alltest

# Cleanup after testing
docker image rm springio/my-movies-api-alltest