#!/bin/bash
# ~~~~~~~ Get Environment
source env.load.sh

# ~~~~~~~ Execution
# Start db service detached
docker compose -f docker-compose.yml up -d --build --force-recreate

# Wait for health-check
while docker ps --all --filter "name=$DEV_DOCKER_CONTAINER_NAME" --format "{{.Status}}" | grep -q "(health: starting)"; do \
  echo "Waiting for container health-check to finish..";
  sleep 5;
done

# Was health-check successful?
container_status=$(docker ps --all --filter "name=$DEV_DOCKER_CONTAINER_NAME" --format "{{.Status}}")
if echo "$container_status" | grep -q "(healthy)"; then
  echo "Docker container $DEV_DOCKER_CONTAINER_NAME is now ready!"
else
  echo "Docker container $DEV_DOCKER_CONTAINER_NAME does not appear to be ready.."
  echo -e "Something might be wrong. See the current status message: \n$container_status"
fi