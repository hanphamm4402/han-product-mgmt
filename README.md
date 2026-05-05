# Han Product Management

## Local Development

Start PostgreSQL and run the application with one deterministic command:

```bash
./scripts/dev-start.sh
```

On Windows PowerShell:

```powershell
.\scripts\dev-start.ps1
```

The script starts the `postgres` service, waits until PostgreSQL reports that the
`han_product_mgmt` database is ready, then runs the Spring Boot application.

If you prefer to run the commands manually:

```bash
docker compose up -d postgres
until docker exec han-product-mgmt-postgres pg_isready -U postgres -d han_product_mgmt; do
  sleep 1
done
./gradlew bootRun
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
./scripts/dev-verify-db.sh
```

On Windows PowerShell:

```powershell
.\scripts\dev-verify-db.ps1
```

This prints the available PostgreSQL databases and the tables inside
`han_product_mgmt`.

## Reset Local Database

Use this only when you intentionally want to delete local database data:

```bash
./scripts/dev-reset-db.sh
```

On Windows PowerShell:

```powershell
.\scripts\dev-reset-db.ps1
```

It runs `docker compose down -v` and then starts PostgreSQL again with a clean
volume.
