#!/usr/bin/env bash
set -euo pipefail

docker exec han-product-mgmt-postgres psql -U postgres -d postgres -c "\\l"
docker exec han-product-mgmt-postgres psql -U postgres -d han_product_mgmt -c "\\dt"
