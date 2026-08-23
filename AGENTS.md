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

## Use Case Structure

Every CRUD operation follows the ENTIC hexagonal pattern. Use case names use the format `{Verb}{Entity}` (e.g. `CreateCompany`, `GetCompanyById`).

### CREATE pattern

```
payroll-core/
├── application/commons/operation/
│   ├── ApplicationRequest.java          (marker interface)
│   ├── ApplicationResponse.java         (marker interface)
│   └── ApplicationUseCase.java          (functional interface: execute(IN) → OUT)
├── application/models/
│   ├── Create{Entity}UseCaseIn.java     (record implements ApplicationRequest)
│   └── Create{Entity}UseCaseOut.java    (record implements ApplicationResponse, wraps nested DTO)
├── application/port/in/
│   └── Create{Entity}UseCase.java       (interface extends ApplicationUseCase<In, Out>)
├── application/impl/
│   └── Create{Entity}UseCaseImpl.java   (implementation, validates, calls port, returns Out)
└── application/port/out/
    └── {Entity}RepositoryPort.java      (save, existsByXxx, etc.)

payroll-api/
├── infrastructure/adapters/out/controller/
│   └── {Entity}Controller.java          (POST uses useCaseHttpExecutor.execute(useCase, input, transformer))
└── infrastructure/mapper/
    └── {Entity}ApiMapper.java           (toCommand, toResponse)

payroll-app/
└── config/{Entity}Config.java           (@Bean wiring: port → adapter, useCase → impl)
```

**Controller pattern for CREATE:**
```java
@PostMapping
public ResponseEntity<?> create(@RequestBody Create{Entity}Request request) {
    return useCaseHttpExecutor.execute(createUseCase,
        {Entity}ApiMapper.toCommand(request),
        out -> {
            URI location = URI.create("/" + entities + "/" + out.{entityCreated}().id());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .location(location)
                    .body(out.{entityCreated}());
        });
}
```

### GET BY ID pattern

```
payroll-core/
├── application/models/
│   ├── Get{Entity}ByIdUseCaseIn.java    (record(UUID id) implements ApplicationRequest)
│   └── Get{Entity}ByIdUseCaseOut.java   (record({Entity}Found) implements ApplicationResponse)
├── application/port/in/
│   └── Get{Entity}ByIdUseCase.java      (interface extends ApplicationUseCase<In, Out>)
└── application/impl/
    └── Get{Entity}ByIdUseCaseImpl.java  (validates id, calls findById, throws NotFoundException)
```

**Controller pattern for GET:**
```java
@GetMapping("/{id}")
public ResponseEntity<?> getById(@PathVariable UUID id) {
    return useCaseHttpExecutor.execute(getByIdUseCase, new Get{Entity}ByIdUseCaseIn(id));
}
```

### Outbound Port pattern

```java
public interface {Entity}RepositoryPort {
    {Entity} save({Entity} entity);
    {Entity} findById(UUID id);           // throws NotFoundException
    boolean existsByXxx(String xxx);      // for uniqueness checks
}
```

### i18n keys

```
{entity}.id.required={Entity} ID is required
{entity}.notFound={Entity} with ID {0} not found
{entity}.xxx.required=Xxx is required
{entity}.xxx.alreadyExists=A {entity} with Xxx {0} already exists
```

### Key conventions

- **Models** go in `application/models/` (not in the entity sub-package).
- **Ports** go in `application/port/in/` (use case interfaces) and `application/port/out/` (repository interfaces).
- **Impls** go in `application/impl/` (not in the entity sub-package).
- **Inbound ports** (use case interfaces) are thin — just `extends ApplicationUseCase<In, Out>`.
- **Transformer**: CREATE uses `HttpResponseTransformer` (for Location header, 201). GET uses direct `execute(useCase, in)` (returns 200 with response body).
- **No annotations** in core — no `@Service`, `@Component`. Wiring is manual via `@Configuration` beans in `payroll-app`.
