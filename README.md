# VitalArc

AI-assisted training load and recovery coach. Logs workouts, computes a real
sports-science training-load metric (acute:chronic workload ratio), and uses
an LLM to turn that data into a weekly coaching recommendation and an
interactive chat coach.

## Why this exists

Most fitness apps log data but never close the loop back to "so what should
I actually do next." VitalArc's AI coach reasons over your recent training
load trend to flag overtraining risk, suggest a deload, or greenlight
pushing harder - with an explanation, not just a badge.

## Architecture

Microservices behind a single API gateway:

- **user-service** - auth (JWT), profiles
- **workout-service** - workout logging, training-load calculation, publishes events
- **ai-coach-service** - Claude API integration, weekly recommendations, chat
- **reporting-service** - scheduled PDF report generation, S3 storage, email
- **gateway** - single entry point, JWT validation, routing
- **frontend** - React + TypeScript SPA

Services communicate synchronously through the gateway and asynchronously
through RabbitMQ for events (e.g. a logged workout triggers a training-load
recalculation).

## Local development

```bash
docker compose up -d postgres redis rabbitmq
cd user-service && ./mvnw spring-boot:run
```

Full docker-compose (all services) lands as each service is completed - see
`docs/roadmap.md` for the build sequence.

## Tech stack

Java 21, Spring Boot 3, Spring Security, Spring Data JPA, PostgreSQL, Redis,
RabbitMQ, React + TypeScript, Docker, AWS (ECS Fargate, RDS, S3, CloudFront),
GitHub Actions, Claude API.

## Status

🚧 Actively being built. See commit history for progress.
