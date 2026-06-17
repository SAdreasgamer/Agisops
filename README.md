# AEGISOPS — Autonomous Engineering Operations Control Plane

AEGISOPS is an enterprise-grade, AI-native engineering operations platform designed to autonomously investigate, analyze, verify, and assist in resolving software engineering operational issues at scale.

This project employs a polyglot microservice architecture using **Spring Boot 4.1 (Java 25)** for the platform backbone and API management, and **Python 3.13 (LangGraph)** for the intelligent multi-agent operation loop.

---

## 🛠️ Project Structure

- `java-services/`: Microservices built on Java 25 LTS, Spring Boot 4.1.0, and Spring Cloud 2025.1.2.
  - `common/`: Shared JPA entities, database repositories (Flyway), Kafka serializers, and common security filters.
  - `api-gateway/`: Reactive API routing gateway with JWT authorization and Redis rate limiting.
  - `auth-service/`: Single Sign-On, OAuth2, and JSON Web Token (JWT) management.
  - `workflow-orchestrator/`: Temporal workflow engine orchestrating investigation state machines.
  - `ingestion-service/`: Entrypoint for external webhooks (GitHub, PagerDuty, AlertManager) publishing raw events to Kafka.
  - `notification-service/`: WebSocket-based client updates and Slack/Email alert relays.
  - `verification-service/`: Automated canary analysis, health checking, and risk-scoring modules.
- `python-services/`: AI agent loops, search routing, and retrieval layers built on Python 3.13.
  - `agent-runtime/`: LangGraph agent superviser and execution loop.
  - `retrieval-service/`: pgvector-backed hybrid search database indexing codebases and log traces.
  - `memory-service/`: Conversational episodic and semantic memory system.
- `proto/`: Shared Protobuf definitions for cross-language (Java ↔ Python) gRPC integrations.

---

## 🚀 Getting Started

### 📋 Prerequisites
- **Java 25 LTS** (or higher)
- **Maven 3.9+**
- **Python 3.13** (with `pip` and virtualenv)
- **Docker & Docker Compose**

### 💻 Local Run Commands
This project includes a root `Makefile` to simplify operations:

1. **Start Infrastructure Services** (PostgreSQL, Redis, Kafka, Temporal):
   ```bash
   make dev
   ```

2. **Build the Java Backends**:
   ```bash
   make java-build
   ```

3. **Run Java Unit/Integration Tests**:
   ```bash
   make java-test
   ```

4. **Run Python Linter**:
   ```bash
   make lint
   ```
