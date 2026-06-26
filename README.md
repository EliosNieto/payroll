# ENTIC Payroll

> 🚀 Building an open-source multi-tenant payroll platform for Colombia in public.

ENTIC Payroll is an enterprise-grade payroll and electronic payroll platform for Colombia, built with **Java 21**, **Spring Boot**, **Domain-Driven Design (DDD)**, **Hexagonal Architecture**, and **Clean Architecture**.

The purpose of this project is not only to build a production-ready payroll system, but also to share the entire software engineering journey publicly through GitHub and LinkedIn.

---

# Vision

Create a modern, scalable and maintainable payroll platform capable of serving multiple companies (SaaS) while complying with Colombian labor regulations and DIAN Electronic Payroll requirements.

This project focuses on:

- Clean code
- SOLID principles
- Domain-Driven Design
- Hexagonal Architecture
- Testability
- Scalability
- Event-driven thinking
- Enterprise software design

---

# Current Status

🚧 Under Development

The project is being built incrementally in public.

---

# High Level Architecture

```
                REST API
                   │
                   ▼
          Application Layer
                   │
                   ▼
             Domain Layer
                   │
        ┌──────────┴──────────┐
        ▼                     ▼
 Persistence             External Services
(PostgreSQL)          (DIAN, Email, Storage)
```

Following **Hexagonal Architecture**, the domain never depends on infrastructure.

---

# Repository Structure

```
entic-payroll
│
├── payroll-api
├── payroll-core
├── payroll-electronic
├── payroll-security
├── payroll-shared
├── payroll-tests
├── docs
└── docker
```

---

# Modules

---

# payroll-api

This module exposes the application through REST APIs.

Responsibilities

- REST Controllers
- Request Validation
- Response Mapping
- Exception Handling
- OpenAPI / Swagger
- Authentication Entry Point

Example

```
POST /employees

GET /employees

POST /payrolls

POST /electronic-payroll/send
```

This module should contain **no business logic**.

---

# payroll-core

The heart of the application.

Contains all payroll business rules.

Responsibilities

## Employee Management

- Employees
- Dependents
- Emergency contacts
- Banks

## Contracts

- Permanent
- Fixed-term
- Apprentice
- Domestic worker
- Integral salary
- Part-time

## Payroll

- Payroll periods
- Payroll calculation
- Earnings
- Deductions
- Benefits
- Net salary

## Overtime

- Day overtime
- Night overtime
- Sunday
- Holidays
- Night surcharge

## Vacations

- Vacation balances
- Vacation requests
- Vacation payments

## Leaves

- Sick leave
- Maternity
- Paternity
- Paid leave
- Unpaid leave

## Benefits

- Severance
- Severance interest
- Service bonus
- Vacations

## Settlement

- Employment termination
- Final settlement
- Indemnities

The core module **knows nothing about databases, REST APIs or DIAN.**

---

# payroll-electronic

Responsible for Colombian Electronic Payroll.

Responsibilities

- XML Generation
- XML Validation
- Digital Signature
- ZIP Compression
- DIAN Integration
- Status Tracking
- Electronic Payroll Adjustments

Main Features

- Generate XML UBL
- Sign XML
- Send XML
- Check status
- Store responses
- Generate CUNE
- Generate adjustment documents

This module is completely isolated from payroll calculations.

---

# payroll-security

Authentication and authorization.

Responsibilities

- JWT
- Spring Security
- Authentication
- Authorization
- Roles
- Permissions
- Password encryption
- Token generation

Roles

- Administrator
- HR
- Accountant
- Supervisor
- Employee

---

# payroll-shared

Shared utilities used by every module.

Responsibilities

## Common Classes

- BaseEntity
- AuditableEntity
- Money
- Address

## Exceptions

- BusinessException
- ValidationException
- NotFoundException

## Utilities

- Date utilities
- Money utilities
- XML utilities
- String utilities

## Constants

- Error codes
- Common enums
- Shared DTOs

---

# payroll-tests

Contains reusable testing utilities.

Responsibilities

- Testcontainers
- Integration Tests
- Builders
- Mock Objects
- Test Data
- Base Test Classes

---

# docs

Project documentation.

Examples

```
Architecture.md

Contributing.md

Coding-Standards.md

Roadmap.md

Decision Records

API Examples
```

---

# docker

Infrastructure for local development.

Contains

- PostgreSQL
- Redis
- Mailpit
- MinIO
- PgAdmin

---

# Architecture Style

The project follows

- Clean Architecture
- Hexagonal Architecture
- Domain-Driven Design
- SOLID
- CQRS (where appropriate)
- Event Driven Design

---

# Layers

```
Controller

↓

Application

↓

Domain

↓

Ports

↓

Infrastructure
```

Dependencies always point inward.

---

# Technologies

Backend

- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate

Database

- PostgreSQL
- Redis

Testing

- JUnit 5
- Mockito
- Testcontainers

Documentation

- OpenAPI
- Swagger

Infrastructure

- Docker
- Docker Compose

Build

- Maven

CI/CD

- GitHub Actions

---

# Planned Features

## Phase 1

- Multi-tenancy
- Employees
- Contracts
- Payroll
- Earnings
- Deductions

---

## Phase 2

- Overtime
- Benefits
- Vacations
- Settlements
- Reports

---

## Phase 3

- Electronic Payroll
- XML Generation
- DIAN Integration
- Digital Signature

---

## Phase 4

- Social Security
- PILA Support
- Accounting Integration
- Notifications

---

# Contributing

Contributions are welcome.

If you find bugs, have suggestions, or want to collaborate, feel free to open an Issue or Pull Request.

---

# Project Goals

This repository aims to demonstrate:

- Enterprise Architecture
- Clean Code
- Spring Boot Best Practices
- Java Best Practices
- Domain Modeling
- Software Design
- Scalable Backend Development

---

# License

MIT License

---

## Follow the Journey

This project is being built in public.

⭐ Star the repository if you'd like to follow the progress.
