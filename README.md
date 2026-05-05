# Han Product Management

## Local Development

Start PostgreSQL:

```bash
docker compose up -d postgres
```

Wait until the database is ready:

```bash
until docker exec han-product-mgmt-postgres pg_isready -U postgres -d han_product_mgmt; do
  sleep 1
done
```

Run the application:

```bash
./gradlew bootRun
```

On Windows PowerShell, use:

```powershell
docker compose up -d postgres

do {
    docker exec han-product-mgmt-postgres pg_isready -U postgres -d han_product_mgmt
    if ($LASTEXITCODE -ne 0) {
        Start-Sleep -Seconds 1
    }
} while ($LASTEXITCODE -ne 0)

.\gradlew.bat bootRun
```

## Database Responsibilities

- Docker/PostgreSQL init creates the `han_product_mgmt` database.
- Flyway creates and versions schema objects such as tables and seed data.
- Hibernate uses `ddl-auto: validate`, so it only checks mappings against the schema.

`POSTGRES_DB` is only applied during the first initialization of a PostgreSQL
volume. If an old volume already exists, Docker will reuse it and PostgreSQL will
skip initialization.

## Debug Database State

```bash
docker exec han-product-mgmt-postgres psql -U postgres -d postgres -c "\\l"
docker exec han-product-mgmt-postgres psql -U postgres -d han_product_mgmt -c "\\dt"
```

On Windows PowerShell:

```powershell
docker exec han-product-mgmt-postgres psql -U postgres -d postgres -c "\l"
docker exec han-product-mgmt-postgres psql -U postgres -d han_product_mgmt -c "\dt"
```

This prints the available PostgreSQL databases and the tables inside
`han_product_mgmt`.

## Reset Local Database

Use this only when you intentionally want to delete local database data:

```bash
docker compose down -v
docker compose up -d postgres
until docker exec han-product-mgmt-postgres pg_isready -U postgres -d han_product_mgmt; do
  sleep 1
done
```

On Windows PowerShell:

```powershell
docker compose down -v
docker compose up -d postgres

do {
    docker exec han-product-mgmt-postgres pg_isready -U postgres -d han_product_mgmt
    if ($LASTEXITCODE -ne 0) {
        Start-Sleep -Seconds 1
    }
} while ($LASTEXITCODE -ne 0)
```

This removes the local PostgreSQL volume and starts PostgreSQL again with a
clean volume.
