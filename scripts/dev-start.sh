#!/usr/bin/env bash
set -euo pipefail

docker compose up -d postgres

until docker exec han-product-mgmt-postgres pg_isready -U postgres -d han_product_mgmt; do
  sleep 1
done

./gradlew bootRun
