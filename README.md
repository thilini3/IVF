# IVF Module Microservice

This project implements a Spring Boot 3.3 microservice that covers the IVF laboratory module described in the comprehensive requirements specification. The service is built with Java 21 and exposes RESTful APIs for managing IVF treatment cycles, laboratory procedures, embryo development, and quality assurance.

## Features

- Treatment cycle lifecycle management with configurable consents and timeline events
- IVF laboratory procedure scheduling, status tracking, and quality checklist enforcement
- Embryo observation and cryopreservation tracking with cryostorage location management
- Quality alert logging and resolution workflow for laboratory incidents
- Integration hooks for FHIR (CarePlan snapshots) and analytics platforms
- Basic HTTP authentication with in-memory users for testing purposes
- H2 in-memory database for rapid prototyping and automated tests
- OpenAPI documentation available at `/swagger-ui.html`

## Technology Stack

- Java 21
- Spring Boot 3.3 (Web, Data JPA, Validation, Security, Actuator)
- H2 in-memory database
- Springdoc OpenAPI
- Maven build system

## Running the Service

```bash
mvn spring-boot:run
```

The service requires HTTP Basic authentication. A default user is provided for local testing:

- Username: `ivf-admin`
- Password: `StrongPassword1!`

## API Highlights

| Area | Endpoint | Description |
| --- | --- | --- |
| Treatment Cycles | `POST /api/cycles` | Create a new IVF treatment cycle |
| | `PATCH /api/cycles/{id}/status` | Update cycle status (e.g., complete, cancel) |
| | `POST /api/cycles/{id}/timeline-events` | Record key timeline milestones |
| Laboratory | `POST /api/cycles/{cycleId}/procedures` | Schedule IVF lab procedures |
| | `PATCH /api/cycles/{cycleId}/procedures/{procedureId}/status` | Update procedure status |
| Embryology | `POST /api/cycles/{cycleId}/embryos` | Record embryo observations |
| | `POST /api/cycles/{cycleId}/embryos/{embryoId}/cryopreservation` | Mark an embryo as cryopreserved |
| Quality | `POST /api/cycles/{cycleId}/quality-alerts` | Log laboratory incidents |
| | `PATCH /api/cycles/{cycleId}/quality-alerts/{alertId}` | Resolve a quality alert |

Refer to the OpenAPI UI for request/response schemas and additional operations.

## Testing

Automated integration tests cover a representative treatment cycle lifecycle:

```bash
mvn test
```

## Further Improvements

- Replace the in-memory authentication with enterprise SSO integration
- Externalize configuration for integration endpoints per environment
- Expand validation rules and regulatory guardrails for additional regions
- Introduce persistence migrations and production-grade database support
