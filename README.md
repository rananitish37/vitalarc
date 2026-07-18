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
- **ai-coach-service** - AI-generated recommendations and chat (currently Gemini API, swappable)
- **reporting-service** - scheduled PDF report generation, S3 storage, email
- **gateway** - single entry point, JWT validation, routing
- **frontend** - React + JavaScript SPA

Services communicate synchronously through the gateway and asynchronously
through RabbitMQ for events (e.g. a logged workout triggers a training-load
recalculation).

## Local development

```bash
docker compose up -d mysql redis rabbitmq
```

Then run each service directly from IntelliJ (fastest for development):
- `UserServiceApplication` — port 8081
- `WorkoutServiceApplication` — port 8082
- `AiCoachServiceApplication` — port 8083 (needs `GEMINI_API_KEY` set as an environment variable)
- `GatewayApplication` — port 8080 (the single entry point your frontend/curl talks to)

Full docker-compose (every service containerized, matching production) lands once the
remaining services and the frontend are built.

## What's built so far

- ✅ **user-service** — registration, login, JWT issuance, BCrypt password hashing
- ✅ **gateway** — JWT validation filter, routes to all three backend services
- ✅ **workout-service** — workout logging, ACWR training-load calculation, publishes
  events to RabbitMQ on every logged workout
- ✅ **ai-coach-service** — consumes workout events, generates AI recommendations and
  handles direct chat via Google's Gemini API (see "AI provider" note below)
- ⏳ **reporting-service** — not yet built (scheduled PDF reports via S3/SES)
- ⏳ **frontend** — not yet built (React + JavaScript)

## AI provider note

Originally planned around the Claude API, but switched to **Google's Gemini API**
(free tier, no credit card) due to budget constraints during development. This was a
one-file change (`GeminiClient` replacing `AnthropicClient`) because `CoachService`
depends on a small `AiClient` interface rather than a concrete provider class -
swapping back to Claude later (or adding it as a second option) means writing one new
class, not touching business logic.

## Try it

```bash
# Register (through the gateway, not directly to user-service)
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"you@test.com","password":"password123","displayName":"You"}'

# Log a workout (replace TOKEN with the accessToken from registration)
curl -X POST http://localhost:8080/api/workouts \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN" \
  -d '{"workoutDate":"2026-07-18","activityType":"Running","durationMinutes":45,"rpe":7}'

# Check your training load
curl http://localhost:8080/api/workouts/training-load \
  -H "Authorization: Bearer TOKEN"

# Get an AI-generated recommendation based on that training load
curl http://localhost:8080/api/coach/recommendation \
  -H "Authorization: Bearer TOKEN"

# Chat with the AI coach directly
curl -X POST http://localhost:8080/api/coach/chat \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN" \
  -d '{"message":"Why is my ratio so low right now?"}'
```

## Tech stack

Java 21, Spring Boot 3, Spring Security, Spring Data JPA, MySQL, Redis,
RabbitMQ, React + JavaScript, Docker, AWS (ECS Fargate, RDS, S3, CloudFront),
GitHub Actions, Google Gemini API.

## Status

🚧 Actively being built. See commit history for progress.