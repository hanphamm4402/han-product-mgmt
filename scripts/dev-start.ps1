$ErrorActionPreference = "Stop"

docker compose up -d postgres

do {
    docker exec han-product-mgmt-postgres pg_isready -U postgres -d han_product_mgmt
    if ($LASTEXITCODE -ne 0) {
        Start-Sleep -Seconds 1
    }
} while ($LASTEXITCODE -ne 0)

.\gradlew.bat bootRun
