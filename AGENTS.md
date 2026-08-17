# AGENTS.md - ENTIC Payroll

## Build Commands

```bash
# Build all modules
mvn clean install

# Compile only
mvn compile

# Run tests (includes I18nKeyCoverageTest)
mvn test

# Build single module
mvn clean install -pl payroll-core -am
```

> Note: `mvn spring-boot:run` is not available yet — there is no main `*Application` class.

## Repository Structure

Multi-module Maven project (Java 21, Spring Boot 3.5.5):

| Module | Purpose |
|--------|---------|
| `payroll-api` | REST controllers, config, exception handling (Spring Web) |
| `payroll-core` | Domain: enums, errors (`ApplicationError`, `ValidationException`, etc.), business rules (hexagonal) |
| `payroll-shared` | Shared utilities: `I18nService`, `resources/i18n/messages.properties` |
| `payroll-security` | Authentication/authorization (empty, pending content) |
| `payroll-electronic` | Electronic payroll / DIAN (empty, pending content) |

## Key Architecture Facts

- **Hexagonal + DDD**: the domain never depends on infrastructure; dependencies point inward (Controller -> Application -> Domain -> Ports -> Infrastructure).
- **Business errors**: `ValidationException`/`NotFoundException`/`AlreadyExistsException`/`AuthException` live in `payroll-core/src/main/java/com/entic/payroll/core/domain/errors/` and are resolved by `GlobalExceptionHandler` in `payroll-api` via `MessageSource`.
- **i18n**: message keys live in `payroll-shared/src/main/resources/i18n/messages.properties`; `I18nService` (`com.entic.payroll.shared.i18n`) wraps `MessageSource`.

## Conventions

- **Logging**: Every class must have a `private static final Logger log = LoggerFactory.getLogger(...)` and log key operations (INFO for actions, DEBUG for details). No class should be silent.
- **i18n**: English only. Keys use **camelCase** (e.g. `error.validationError`, `order.notFound`) in `payroll-shared/src/main/resources/i18n/messages.properties`. Every key passed to `ValidationException`/`NotFoundException`/`AlreadyExistsException` must exist in that file — enforced by `I18nKeyCoverageTest` (scans `payroll-core/src/main/java`).
- **Error codes**: camelCase (`validationError`, `notFound`, `alreadyExists`, `authError`).
- **English-only naming (no Spanglish)**: All identifiers must be 100% English — Java fields/methods, DB columns, i18n keys, enum values, JSON/DTO fields. Never mix Spanish and English in a single name (e.g. `baseSalary`, NOT `salarioBase`; `documentNumber`, NOT `numeroDocumento`). One concept = one language (English).

## Gotchas

- There is no runnable application yet — only compilable layers.
- `payroll-security` and `payroll-electronic` have only their `pom.xml` (empty modules).
- The i18n coverage test fails if a key used in `payroll-core` has no entry in `messages.properties`.
- JPA persistence is pending (see `jpa.md`): when created, use Flyway with `spring.jpa.hibernate.ddl-auto=validate` and English naming for tables/columns.
