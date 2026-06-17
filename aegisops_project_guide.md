# AEGISOPS — Autonomous Engineering Operations Control Plane

## Complete Project Guide & Detailed Phase-Wise Implementation Plan

### Tech Stack: Spring Boot (Java 21) + Python 3.12 Hybrid Architecture

---

# Table of Contents

1. [Introduction](#1-introduction)
2. [Vision & Mission](#2-vision--mission)
3. [Problem Statement](#3-problem-statement)
4. [Core Features Overview](#4-core-features-overview)
5. [High-Level Architecture](#5-high-level-architecture)
6. [System Design Philosophy](#6-system-design-philosophy)
7. [Microservices Breakdown](#7-microservices-breakdown)
8. [Multi-Agent AI Architecture](#8-multi-agent-ai-architecture)
9. [Event-Driven Architecture](#9-event-driven-architecture)
10. [Memory System Design](#10-memory-system-design)
11. [RAG Pipeline](#11-rag-pipeline)
12. [Backend Engineering](#12-backend-engineering)
13. [Database Design](#13-database-design)
14. [Queue & Workflow Systems](#14-queue--workflow-systems)
15. [Authentication & RBAC](#15-authentication--rbac)
16. [Security Architecture](#16-security-architecture)
17. [Observability Stack](#17-observability-stack)
18. [Real-Time Streaming System](#18-real-time-streaming-system)
19. [Deployment Architecture](#19-deployment-architecture)
20. [Kubernetes Infrastructure](#20-kubernetes-infrastructure)
21. [CI/CD Pipeline](#21-cicd-pipeline)
22. [AI Evaluation System](#22-ai-evaluation-system)
23. [Cost Optimization](#23-cost-optimization)
24. [Folder Structure](#24-folder-structure)
25. [API Design](#25-api-design)
26. [Scaling Considerations](#26-scaling-considerations)
27. [Failure Scenarios & Resilience](#27-failure-scenarios--resilience)
28. [Enterprise Features](#28-enterprise-features)
29. [Future Enhancements](#29-future-enhancements)
30. [Detailed Phase-Wise Implementation Plan](#30-detailed-phase-wise-implementation-plan)
31. [Resume Impact](#31-resume-impact)
32. [Final Notes](#32-final-notes)

---

# 1. Introduction

AEGISOPS is an enterprise-grade, AI-native engineering operations platform designed to autonomously investigate, analyze, verify, and assist in resolving software engineering operational issues at scale.

The platform continuously monitors and reasons across:

| System Category | Examples |
|---|---|
| **CI/CD Systems** | GitHub Actions, Jenkins, GitLab CI, CircleCI |
| **Container Orchestration** | Kubernetes clusters, pod health, resource utilization |
| **Infrastructure Metrics** | CPU, memory, disk, network, custom application metrics |
| **Logs** | Application logs, system logs, audit logs |
| **Alerts** | PagerDuty, OpsGenie, Datadog alerts |
| **Code Repositories** | GitHub, GitLab — commits, PRs, diffs |
| **Deployment Systems** | ArgoCD, Spinnaker, Flux |
| **Incident Management** | Jira, ServiceNow, PagerDuty incidents |

AEGISOPS acts as an **intelligent operational layer** between engineering systems and developers — not a chatbot wrapper, but a distributed, event-driven, workflow-oriented, multi-agent platform with persistent memory, enterprise-grade security, and observability-first design.

### Polyglot Architecture Philosophy

AEGISOPS uses a **hybrid Spring Boot + Python** architecture — the same pattern used at companies like Netflix, Uber, and Google:

| Layer | Technology | Rationale |
|---|---|---|
| **Infrastructure Backbone** | Spring Boot (Java 21) | Enterprise-grade APIs, battle-tested security, first-class Kafka/Temporal support, strong typing |
| **AI/ML Services** | Python 3.12 | LangGraph, LangChain, sentence-transformers — the AI/ML ecosystem is Python-first |
| **Cross-Service Communication** | Kafka + gRPC (Protobuf) | Language-agnostic contracts, high performance, type-safe |

### What Makes AEGISOPS Different

| Dimension | Typical AI Tools | AEGISOPS |
|---|---|---|
| **Architecture** | Monolithic API wrapper | Polyglot distributed microservices + event bus |
| **Intelligence** | Single LLM call | Multi-agent coordination with reflection |
| **State** | Stateless | Short-term + long-term memory with compression |
| **Verification** | None — trusts LLM output | Autonomous verification pipeline |
| **Reliability** | No retry, no DLQ | Saga pattern, circuit breakers, DLQs |
| **Security** | API key auth | Zero-trust, Spring Security RBAC, sandboxed execution |
| **Observability** | Console logs | OpenTelemetry + Prometheus + Grafana + Tempo |

---

# 2. Vision & Mission

> **Vision:** Build an autonomous engineering operations platform capable of reducing operational debugging time from hours to minutes.

### Mission Pillars

1. **Incident Reasoning** — Understand the "why" behind failures, not just the "what"
2. **Root Cause Analysis** — Correlate signals across CI/CD, Kubernetes, metrics, logs, and code changes to identify true root causes
3. **Autonomous Remediation Proposals** — Generate verified, safe remediation plans — not hallucinated suggestions
4. **Verification-Driven Fixes** — Every proposed fix goes through automated verification before human approval
5. **Intelligent Deployment Safety** — Analyze deployments for risk, run canary verification, and recommend rollbacks when necessary
6. **Continuous Engineering Optimization** — Learn from past incidents to improve future response and prevent recurrence

### Non-Goals (Equally Important)

- AEGISOPS does NOT auto-deploy to production without human approval (safety-first)
- AEGISOPS does NOT replace monitoring tools — it enhances them with reasoning
- AEGISOPS does NOT perform arbitrary code generation — it focuses on operational investigation and remediation

---

# 3. Problem Statement

### The Real Cost of Operational Toil

Modern engineering organizations suffer from:

| Problem | Impact |
|---|---|
| **Build failures** | Developer time wasted debugging CI |
| **Flaky tests** | False confidence, delayed releases |
| **Production incidents** | Revenue loss, customer trust erosion |
| **Infrastructure drift** | Silent config deviations cause outages |
| **Alert fatigue** | Engineers ignore critical signals in noise |
| **Knowledge fragmentation** | Runbooks scattered, tribal knowledge lost |
| **Long MTTR** | Hours spent correlating GitHub + Jenkins + K8s + Datadog + PagerDuty + Jira + Slack |
| **Tool fragmentation** | 8+ tools to investigate a single incident |

### The Correlation Problem

To investigate a single production incident, an engineer manually correlates:

```
GitHub (recent commits)
  → Jenkins/GitHub Actions (build status)
    → Kubernetes (pod health, events, logs)
      → Datadog/Prometheus (metrics anomalies)
        → PagerDuty (alert context)
          → Jira (related tickets)
            → Slack (team discussions)
              → Internal runbooks (remediation steps)
```

This consumes **massive engineering hours** and requires deep institutional knowledge.

### The Gap in Current Tooling

```
Monitoring tools     → Detect problems (Datadog, Prometheus)
CI tools             → Detect build failures (GitHub Actions, Jenkins)
AI copilots          → Generate text responses (ChatGPT, Copilot)
```

**But no system performs:**

- ✅ Cross-system reasoning (correlating signals from 5+ sources)
- ✅ Workflow orchestration (multi-step investigation with branching logic)
- ✅ Verification-driven autonomous investigation (proving root causes, not guessing)
- ✅ Persistent learning from past incidents

**AEGISOPS fills this gap.**

---

# 4. Core Features Overview

### Incident Intelligence
- Root cause analysis across multiple data sources
- Failure clustering — group related incidents automatically
- Similar incident retrieval — "we've seen this pattern before"
- Cross-system correlation — connect GitHub commits → build failures → pod crashes

### Autonomous Investigation
- Automated log analysis with semantic reasoning
- Deployment analysis — diff what changed
- Commit inspection — identify suspicious code changes
- Test failure reasoning — explain why tests fail and suggest fixes

### Multi-Agent Coordination
- **Supervisor Agent** — orchestrates the workflow lifecycle
- **Planner Agent** — creates investigation DAGs
- **Retrieval Agent** — fetches relevant context from all sources
- **Executor Agent** — runs safe read-only inspections
- **Critic Agent** — challenges assumptions, detects hallucinations
- **Verification Agent** — validates proposed fixes
- **Reflection Agent** — learns from outcomes

### RAG-Powered Context Retrieval
- Codebase retrieval (function-level chunking)
- Runbook retrieval (step-level chunking)
- Historical incidents (semantic similarity search)
- Architecture documentation (paragraph-level chunking)

### Autonomous Verification
- Test verification — run targeted tests to validate hypotheses
- Canary analysis — evaluate deployment health
- Deployment verification — compare before/after metrics
- Safety scoring — confidence-weighted risk assessment

### Workflow Engine
- Long-running workflows with checkpointing
- Distributed execution across worker pools
- Retry systems with exponential backoff + jitter
- Dead-letter queues for failed task inspection

### Real-Time Operations Dashboard
- Live workflow tracing — follow investigations in real-time
- Streaming logs — watch agent reasoning unfold
- Agent reasoning visualization — see the multi-agent graph
- Incident timelines — unified view across all systems

### Enterprise Security
- Role-Based Access Control (RBAC) with 6 roles
- OAuth2/OIDC with SSO support (Spring Security)
- Complete audit logging of every agent action
- Policy enforcement — configurable guardrails
- Sandboxed execution — Firecracker microVMs / Docker isolation

---

# 5. High-Level Architecture

```
┌───────────────────────────────────────────────────────────┐
│                   Frontend Dashboard                       │
│              (React/Next.js + WebSockets)                   │
└────────────────────────┬──────────────────────────────────┘
                         │ HTTPS / WSS
                         ▼
┌───────────────────────────────────────────────────────────┐
│              API Gateway [Spring Boot]                      │
│     (Spring Cloud Gateway — routing, auth, rate limiting)  │
└────────────────────────┬──────────────────────────────────┘
                         │
        ┌────────────────┼────────────────┬─────────────────┐
        ▼                ▼                ▼                 ▼
┌──────────────┐ ┌──────────────┐ ┌──────────────┐ ┌──────────────┐
│  Auth        │ │  Workflow    │ │  Retrieval   │ │ Notification │
│  Service     │ │  Orchestrator│ │  Service     │ │ Service      │
│ [Spring Boot]│ │ [Spring Boot]│ │  [Python]    │ │ [Spring Boot]│
│ Spring Sec.  │ │ Temporal SDK │ │  FastAPI     │ │ Spring WS    │
└──────────────┘ └──────┬───────┘ └──────────────┘ └──────────────┘
                        │
                        ▼
┌───────────────────────────────────────────────────────────┐
│                Event Bus (Apache Kafka)                     │
│                                                            │
│  Topics: ci.build.failed, deployment.failed,               │
│          k8s.pod.crashloop, metric.latency.spike,          │
│          incident.created, agent.plan.generated, ...       │
└────────────────────────┬──────────────────────────────────┘
                         │
        ┌────────────────┼────────────────┬─────────────────┐
        ▼                ▼                ▼                 ▼
┌──────────────┐ ┌──────────────┐ ┌──────────────┐ ┌──────────────┐
│  AI Agent    │ │  Executor    │ │ Verification │ │  Memory      │
│  Runtime     │ │  Workers     │ │ Service      │ │  Service     │
│  [Python]    │ │  [Python]    │ │ [Spring Boot]│ │  [Python]    │
│  LangGraph   │ │  Sandboxed   │ │ Spring Boot  │ │  FastAPI     │
└──────┬───────┘ └──────────────┘ └──────────────┘ └──────────────┘
       │
       ▼
┌───────────────────────────────────────────────────────────┐
│              Data Layer                                     │
│                                                            │
│  ┌─────────┐  ┌──────────┐  ┌──────────┐  ┌────────────┐ │
│  │PostgreSQL│  │ pgvector │  │  Redis   │  │ Object     │ │
│  │(Workflows│  │(Embeddings│  │(Cache,   │  │ Storage    │ │
│  │Incidents)│  │Semantic  │  │Sessions) │  │ (S3/MinIO) │ │
│  └─────────┘  │Memory)   │  └──────────┘  └────────────┘ │
│               └──────────┘                                 │
└───────────────────────────────────────────────────────────┘
```

### Service Technology Map

| Service | Language | Framework | Why This Choice |
|---|---|---|---|
| **API Gateway** | Java 21 | Spring Cloud Gateway | Enterprise routing, filters, rate limiting |
| **Auth Service** | Java 21 | Spring Boot + Spring Security | OAuth2/OIDC/SAML battle-tested |
| **Workflow Orchestrator** | Java 21 | Spring Boot + Temporal Java SDK | First-class Temporal support, typed workflows |
| **Ingestion Service** | Java 21 | Spring Boot + Spring Kafka | Robust Kafka consumer/producer |
| **Notification Service** | Java 21 | Spring Boot + Spring WebSocket | WebSocket, Mail, Slack integrations |
| **Verification Service** | Java 21 | Spring Boot | Business logic, safety scoring, policy checks |
| **Agent Runtime** | Python 3.12 | LangGraph + FastAPI | LangGraph is Python-only, no Java equivalent |
| **Retrieval Service** | Python 3.12 | FastAPI | sentence-transformers, chunking libs, reranking |
| **Memory Service** | Python 3.12 | FastAPI | Tightly coupled with agent runtime + vector ops |
| **Embedding Workers** | Python 3.12 | Celery / Kafka consumer | Batch embedding, tiktoken, chunking |

### Cross-Language Communication

```
┌──────────────────┐         ┌──────────────────┐
│  Spring Boot     │  gRPC   │  Python           │
│  Services        │◄───────►│  AI Services      │
│                  │ Protobuf│                    │
│  (Java 21)       │         │  (Python 3.12)    │
└────────┬─────────┘         └────────┬───────────┘
         │                            │
         └──────────┬─────────────────┘
                    ▼
         ┌──────────────────┐
         │   Apache Kafka   │
         │  (Async Events)  │
         └──────────────────┘
```

**Two communication patterns:**
1. **Kafka** — Async event-driven communication (primary). All workflow events, agent tasks, notifications.
2. **gRPC** — Synchronous request-response when needed (e.g., API gateway calling retrieval service for search). Shared `.proto` files ensure type safety across Java and Python.

---

# 6. System Design Philosophy

### Architecture Style

| Principle | Implementation |
|---|---|
| **Polyglot Microservices** | Java for infra backbone, Python for AI — right tool for each job |
| **Event-Driven** | All workflows originate from Kafka events, processed asynchronously |
| **Distributed Workflows** | Long-running investigations with Temporal checkpointing and retry |
| **AI-First** | AI agents are first-class citizens with tool access, not afterthoughts |
| **Verification-Driven** | Every AI output is verified before action |
| **Contract-First** | Protobuf definitions shared across Java and Python services |

### Why Polyglot?

This is not an arbitrary choice — it mirrors how FAANG companies actually build AI-integrated platforms:

| Company | Pattern |
|---|---|
| **Netflix** | Java microservices + Python ML pipelines |
| **Uber** | Go/Java services + Python ML/routing |
| **Google** | C++/Java infrastructure + Python for ML |
| **LinkedIn** | Java services + Python for AI features |

The AI/ML ecosystem (LangGraph, LangChain, sentence-transformers, tiktoken) is **Python-first** with no mature Java equivalents. Forcing Java here means reinventing wheels or using immature libraries. Meanwhile, Spring Boot is the gold standard for enterprise Java services.

### Why Event-Driven?

Engineering systems are naturally event-driven:

```
ci.build.failed        →  Trigger investigation workflow
deployment.completed   →  Run canary verification
k8s.pod.crashloop      →  Analyze pod logs + recent deploys
metric.latency.spike   →  Correlate with deployments + infra changes
alert.triggered        →  Enrich context + route to relevant agent
pr.merged              →  Track change for future correlation
test.flaky.detected    →  Start flaky test investigation
```

All AEGISOPS workflows originate from Kafka events. Kafka decouples producers from consumers, enabling independent scaling, failure isolation, **and language independence** — a Java producer and Python consumer work seamlessly.

### Design Constraints

1. **No synchronous LLM calls in the request path** — all AI inference is async via Kafka workers
2. **Every state mutation goes through Kafka** — auditability by design
3. **Agents never execute write operations without approval** — safety-first
4. **All services are stateless** — state lives in databases and queues
5. **Every service emits OpenTelemetry traces** — Micrometer (Java) + opentelemetry-python
6. **Contract-first API design** — Protobuf for inter-service, OpenAPI for external

---

# 7. Microservices Breakdown

---

## 7.1 API Gateway [Spring Boot]

**Purpose:** Single entry point for all external requests.

| Responsibility | Details |
|---|---|
| Routing | Path-based routing to internal services |
| Authentication | JWT validation via Spring Security |
| Rate Limiting | Per-user, per-endpoint limits (Resilience4j) |
| Request Tracing | Inject trace ID (Micrometer + OpenTelemetry) |
| Tenant Routing | Multi-tenant request isolation |

**Technology:** Spring Cloud Gateway + Spring Security

**Key Configuration:**
```yaml
# application.yml
spring:
  cloud:
    gateway:
      routes:
        - id: workflow-service
          uri: lb://workflow-orchestrator
          predicates:
            - Path=/api/v1/workflows/**
          filters:
            - name: RequestRateLimiter
              args:
                redis-rate-limiter.replenishRate: 10
                redis-rate-limiter.burstCapacity: 20
            - name: AddRequestHeader
              args:
                name: X-Trace-Id
                value: "#{T(java.util.UUID).randomUUID().toString()}"
        
        - id: retrieval-service
          uri: http://retrieval-service:8000  # Python FastAPI
          predicates:
            - Path=/api/v1/search/**

        - id: agent-runtime
          uri: http://agent-runtime:8000  # Python FastAPI
          predicates:
            - Path=/api/v1/agents/**
```

---

## 7.2 Auth Service [Spring Boot]

**Purpose:** Identity, authentication, and authorization.

| Responsibility | Details |
|---|---|
| OAuth2/OIDC | Google, GitHub, Enterprise SSO (SAML) |
| JWT Issuance | Short-lived access tokens + refresh tokens |
| Session Management | Token lifecycle, revocation |
| RBAC Enforcement | Permission checks per endpoint |

**Technology:** Spring Boot + Spring Security OAuth2 Resource Server

**Spring Security Configuration:**
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter()))
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/workflows/*/approve").hasAnyRole("ADMIN", "SRE", "PLATFORM_ENGINEER")
                .requestMatchers("/api/v1/workflows/**").hasAnyRole("ADMIN", "SRE", "PLATFORM_ENGINEER", "DEVELOPER")
                .requestMatchers("/api/v1/audit/**").hasAnyRole("ADMIN", "AUDITOR")
                .requestMatchers("/api/v1/dashboard/**").authenticated()
                .anyRequest().authenticated()
            )
            .build();
    }
}
```

**RBAC Roles:**
```
ADMIN              → Full platform control
PLATFORM_ENGINEER  → Manage workflows, view all incidents
SRE                → Approve remediations, manage infrastructure
DEVELOPER          → View own team's incidents, trigger investigations
VIEWER             → Read-only dashboard access
AUDITOR            → Access audit logs, compliance reports
```

**Permission Matrix:**

| Action | Admin | Platform Eng | SRE | Developer | Viewer | Auditor |
|---|---|---|---|---|---|---|
| Create workflow | ✅ | ✅ | ✅ | ✅ | ❌ | ❌ |
| Approve remediation | ✅ | ✅ | ✅ | ❌ | ❌ | ❌ |
| View all incidents | ✅ | ✅ | ✅ | ❌ | ❌ | ✅ |
| Manage RBAC | ✅ | ❌ | ❌ | ❌ | ❌ | ❌ |
| View audit logs | ✅ | ❌ | ❌ | ❌ | ❌ | ✅ |
| Configure agents | ✅ | ✅ | ❌ | ❌ | ❌ | ❌ |

---

## 7.3 Workflow Orchestrator [Spring Boot]

**Purpose:** Orchestrate long-running, multi-step investigation workflows.

| Responsibility | Details |
|---|---|
| Workflow DAG Execution | Define and execute directed acyclic graphs of tasks |
| State Management | Persist workflow state for resume-after-failure |
| Agent Coordination | Publish tasks to Kafka for Python agent runtime |
| Retry Orchestration | Handle transient failures with configurable policies |
| Human-in-the-Loop | Pause workflows for human approval via Temporal signals |

**Technology:** Spring Boot + Temporal Java SDK

**Why Temporal over alternatives:**
- **vs. Celery:** Temporal has built-in state management, retry, and saga support
- **vs. Airflow:** Airflow is batch-oriented; Temporal handles event-driven, long-running workflows
- **vs. Step Functions:** Temporal is open-source and cloud-agnostic
- **Java SDK is first-class** — same maturity as Python SDK

**Example Workflow Definition (Java):**
```java
@WorkflowInterface
public interface IncidentInvestigationWorkflow {
    @WorkflowMethod
    InvestigationResult investigate(IncidentEvent incident);

    @SignalMethod
    void approveRemediation(ApprovalDecision decision);

    @QueryMethod
    WorkflowStatus getStatus();
}

@WorkflowImpl(taskQueues = "aegisops-investigations")
public class IncidentInvestigationWorkflowImpl implements IncidentInvestigationWorkflow {

    private final InvestigationActivities activities = Workflow.newActivityStub(
        InvestigationActivities.class,
        ActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofMinutes(5))
            .setRetryOptions(RetryOptions.newBuilder()
                .setMaximumAttempts(3)
                .setInitialInterval(Duration.ofSeconds(2))
                .setBackoffCoefficient(2.0)
                .build())
            .build()
    );

    private boolean approvalReceived = false;
    private ApprovalDecision approvalDecision;

    @Override
    public InvestigationResult investigate(IncidentEvent incident) {
        // Step 1: Publish planning task to Kafka → Python agent runtime picks it up
        InvestigationPlan plan = activities.planInvestigation(incident);

        // Step 2: Retrieve context (via gRPC call to Python retrieval service)
        RetrievalResult context = activities.retrieveContext(plan);

        // Step 3: Execute AI agent graph (Kafka → Python agent runtime → Kafka response)
        AnalysisResult analysis = activities.executeAgentAnalysis(plan, context);

        // Step 4: Verification (Spring Boot verification service)
        VerificationResult verification = activities.verifyAnalysis(analysis);

        // Step 5: Human approval if remediation proposed
        if (analysis.hasRemediation()) {
            Workflow.await(Duration.ofHours(4), () -> approvalReceived);
            if (!approvalReceived) {
                return InvestigationResult.timeout(analysis);
            }
        }

        return InvestigationResult.completed(analysis, verification);
    }

    @Override
    public void approveRemediation(ApprovalDecision decision) {
        this.approvalDecision = decision;
        this.approvalReceived = true;
    }

    @Override
    public WorkflowStatus getStatus() {
        return currentStatus;
    }
}
```

**Temporal Activities (Bridge to Python):**
```java
@ActivityInterface
public interface InvestigationActivities {
    
    // This activity publishes to Kafka topic "agents.plan" and waits for response
    InvestigationPlan planInvestigation(IncidentEvent incident);
    
    // This calls Python retrieval service via gRPC
    RetrievalResult retrieveContext(InvestigationPlan plan);
    
    // This publishes to Kafka topic "agents.execute" and waits for response
    AnalysisResult executeAgentAnalysis(InvestigationPlan plan, RetrievalResult context);
    
    // This runs in Spring Boot verification service directly
    VerificationResult verifyAnalysis(AnalysisResult analysis);
}
```

---

## 7.4 Ingestion Service [Spring Boot]

**Purpose:** Receive webhooks from external systems and publish to Kafka.

**Technology:** Spring Boot + Spring Kafka

```java
@RestController
@RequestMapping("/api/v1/webhooks")
public class WebhookController {

    private final KafkaTemplate<String, AegisEvent> kafkaTemplate;
    private final EventDeduplicationService deduplicationService;

    @PostMapping("/github")
    public ResponseEntity<EventAck> handleGitHubWebhook(
            @RequestBody Map<String, Object> payload,
            @RequestHeader("X-GitHub-Event") String eventType,
            @RequestHeader("X-Hub-Signature-256") String signature) {
        
        // 1. Verify webhook signature
        webhookVerifier.verify(payload, signature);
        
        // 2. Parse into domain event
        AegisEvent event = githubEventParser.parse(eventType, payload);
        
        // 3. Deduplicate
        if (deduplicationService.isDuplicate(event)) {
            return ResponseEntity.ok(EventAck.duplicate(event.getEventId()));
        }
        
        // 4. Publish to Kafka
        kafkaTemplate.send("aegisops.events.raw", event.getEventId(), event);
        
        return ResponseEntity.accepted()
            .body(EventAck.accepted(event.getEventId()));
    }

    @PostMapping("/pagerduty")
    public ResponseEntity<EventAck> handlePagerDutyWebhook(@RequestBody Map<String, Object> payload) { ... }

    @PostMapping("/alertmanager")
    public ResponseEntity<EventAck> handleAlertManagerWebhook(@RequestBody Map<String, Object> payload) { ... }
}
```

**Event Deduplication (Redis-based):**
```java
@Service
public class EventDeduplicationService {

    private final StringRedisTemplate redisTemplate;

    public boolean isDuplicate(AegisEvent event) {
        String key = "dedup:" + event.getDeduplicationKey();
        Boolean wasSet = redisTemplate.opsForValue()
            .setIfAbsent(key, "1", Duration.ofHours(1));
        return Boolean.FALSE.equals(wasSet);
    }
}
```

---

## 7.5 Notification Service [Spring Boot]

**Purpose:** Multi-channel notifications and real-time WebSocket streaming.

**Technology:** Spring Boot + Spring WebSocket + Spring Mail

```java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
            .setAllowedOrigins("*")
            .withSockJS();
    }
}

@Service
public class WorkflowNotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final SlackClient slackClient;

    // Kafka listener — receives workflow events and broadcasts to WebSocket clients
    @KafkaListener(topics = "aegisops.workflow.events")
    public void onWorkflowEvent(WorkflowEvent event) {
        // Broadcast to WebSocket subscribers
        messagingTemplate.convertAndSend(
            "/topic/workflows/" + event.getWorkflowId(),
            event
        );
        
        // Send Slack notification for important events
        if (event.getType().equals("WORKFLOW_COMPLETED") || event.getType().equals("WORKFLOW_FAILED")) {
            slackClient.sendInvestigationUpdate(event);
        }
    }
}
```

---

## 7.6 Verification Service [Spring Boot]

**Purpose:** Validate every AI-generated analysis and remediation.

**Technology:** Spring Boot (business logic, no AI dependencies needed)

```java
@Service
public class VerificationService {

    public VerificationResult verify(AnalysisResult analysis) {
        List<VerificationCheck> checks = new ArrayList<>();
        
        // 1. Evidence completeness check
        checks.add(checkEvidenceCompleteness(analysis));
        
        // 2. Root cause confidence scoring
        checks.add(scoreRootCauseConfidence(analysis));
        
        // 3. Remediation safety check
        if (analysis.hasRemediation()) {
            checks.add(scoreSafety(analysis.getRemediation()));
        }
        
        // 4. Aggregate verdict
        double overallConfidence = checks.stream()
            .mapToDouble(VerificationCheck::getConfidence)
            .average()
            .orElse(0.0);
        
        return VerificationResult.builder()
            .checks(checks)
            .overallConfidence(overallConfidence)
            .verdict(overallConfidence > 0.7 ? Verdict.PASS : Verdict.NEEDS_REVIEW)
            .build();
    }
}

@Service
public class SafetyScorer {

    public SafetyScore score(RemediationProposal remediation) {
        int score = 100;
        if (remediation.touchesProductionConfig()) score -= 20;
        if (remediation.changesDatabaseSchema()) score -= 30;
        if (remediation.affectsMultipleServices()) score -= 15;
        if (!remediation.hasRollbackPlan()) score -= 25;
        if (remediation.hasTestCoverage()) score += 10;
        return new SafetyScore(Math.max(0, Math.min(100, score)));
    }
}
```

---

## 7.7 Agent Runtime [Python]

**Purpose:** Execute AI agents with tool access and reflection.

**Technology:** Python 3.12 + LangGraph + FastAPI

| Responsibility | Details |
|---|---|
| Agent Execution | Run agents with appropriate tools and context |
| Prompt Orchestration | Construct prompts from memory + retrieval + system instructions |
| Tool Invocation | Safely invoke tools (kubectl, git, API calls) |
| Reflection Loops | Agents evaluate their own reasoning quality |

**Why Python is non-negotiable here:** LangGraph, LangChain, and the entire multi-agent orchestration ecosystem are Python-only. Spring AI / LangChain4j do not support stateful agent graphs with conditional routing, supervisor patterns, or reflection loops.

```python
# services/agent-runtime/graph.py
from langgraph.graph import StateGraph, END

class InvestigationState(TypedDict):
    incident: dict
    plan: list[dict]
    retrieved_context: list[str]
    agent_thoughts: list[str]
    analysis: dict
    confidence: float
    next_agent: str

def build_investigation_graph() -> StateGraph:
    graph = StateGraph(InvestigationState)
    
    graph.add_node("supervisor", supervisor_node)
    graph.add_node("planner", planner_node)
    graph.add_node("retrieval", retrieval_node)
    graph.add_node("executor", executor_node)
    graph.add_node("critic", critic_node)
    graph.add_node("verification", verification_node)
    
    graph.set_entry_point("supervisor")
    graph.add_conditional_edges("supervisor", route_to_agent)
    
    for agent in ["planner", "retrieval", "executor", "critic", "verification"]:
        graph.add_edge(agent, "supervisor")
    
    return graph.compile()
```

**Kafka Integration (receives tasks from Spring Boot):**
```python
# services/agent-runtime/kafka_consumer.py
from aiokafka import AIOKafkaConsumer, AIOKafkaProducer

async def consume_agent_tasks():
    consumer = AIOKafkaConsumer("agents.execute", bootstrap_servers="kafka:9092")
    producer = AIOKafkaProducer(bootstrap_servers="kafka:9092")
    
    async for msg in consumer:
        task = json.loads(msg.value)
        
        # Run LangGraph investigation
        result = await investigation_graph.ainvoke(task)
        
        # Publish result back to Kafka for Spring Boot to consume
        await producer.send(
            "agents.results",
            key=task["workflow_id"].encode(),
            value=json.dumps(result).encode()
        )
```

---

## 7.8 Retrieval Service [Python]

**Purpose:** Hybrid search across all engineering knowledge.

**Technology:** Python 3.12 + FastAPI + sentence-transformers + pgvector

| Responsibility | Details |
|---|---|
| Hybrid Search | Combine vector (semantic) + keyword (BM25) search |
| Vector Retrieval | Embed queries and find similar documents |
| Semantic Ranking | Re-rank results using cross-encoder models |
| Query Expansion | Expand ambiguous queries with LLM assistance |
| Source Filtering | Filter by data source, time range, team |

**gRPC Server (called by Spring Boot services):**
```python
# services/retrieval/grpc_server.py
class RetrievalServicer(retrieval_pb2_grpc.RetrievalServiceServicer):
    
    async def Search(self, request, context):
        results = await hybrid_search.search(
            query=request.query,
            top_k=request.top_k,
            filters=parse_filters(request.filters),
        )
        return retrieval_pb2.SearchResponse(
            results=[to_proto(r) for r in results]
        )
```

---

## 7.9 Memory Service [Python]

**Purpose:** Manage short-term and long-term AI agent memory.

**Technology:** Python 3.12 + FastAPI + Redis + pgvector

```python
class MemoryService:
    def __init__(self, redis_client, pg_pool, vector_store):
        self.short_term = RedisMemoryStore(redis_client)    # TTL: 24 hours
        self.long_term = PostgresMemoryStore(pg_pool)       # Permanent
        self.semantic = VectorMemoryStore(vector_store)     # Searchable
    
    async def store_workflow_context(self, workflow_id: str, context: dict):
        await self.short_term.set(f"wf:{workflow_id}", context, ttl=86400)
    
    async def commit_to_long_term(self, incident_id: str, resolution: dict):
        await self.long_term.insert(incident_id, resolution)
        embedding = await self.embed(resolution["summary"])
        await self.semantic.upsert(incident_id, embedding, resolution)
    
    async def recall_similar(self, query: str, top_k: int = 5) -> list:
        embedding = await self.embed(query)
        return await self.semantic.search(embedding, top_k=top_k)
```

---

# 8. Multi-Agent AI Architecture

AEGISOPS uses a **supervisor-driven multi-agent architecture** where specialized agents collaborate on investigation tasks. All agents run in the **Python agent runtime** and communicate with the Spring Boot backbone via Kafka.

### Agent Interaction Graph

```
                    ┌──────────────┐
                    │  Supervisor  │
                    │    Agent     │
                    └──────┬───────┘
                           │
              ┌────────────┼────────────────┐
              ▼            ▼                ▼
        ┌──────────┐ ┌──────────┐    ┌──────────┐
        │ Planner  │ │ Critic   │    │Reflection│
        │ Agent    │ │ Agent    │    │  Agent   │
        └────┬─────┘ └──────────┘    └──────────┘
             │
    ┌────────┼────────┐
    ▼        ▼        ▼
┌────────┐ ┌────────┐ ┌────────────┐
│Retrieval│ │Executor│ │Verification│
│ Agent  │ │ Agent  │ │   Agent    │
└────────┘ └────────┘ └────────────┘
```

### Supervisor Agent

**Role:** Controls the entire workflow lifecycle — the "manager" of all agents.

```python
class SupervisorAgent:
    async def route(self, state: WorkflowState) -> str:
        if state.needs_planning:
            return "planner"
        elif state.needs_retrieval:
            return "retrieval"
        elif state.needs_execution:
            return "executor"
        elif state.needs_verification:
            return "verification"
        elif state.needs_critique:
            return "critic"
        elif state.confidence < 0.7:
            return "escalate_to_human"
        else:
            return "complete"
```

### Planner Agent

**Role:** Creates investigation DAGs.

```json
{
  "plan_id": "plan_001",
  "steps": [
    {"step": 1, "action": "fetch_pipeline_logs", "agent": "retrieval"},
    {"step": 2, "action": "compare_similar_incidents", "agent": "retrieval", "depends_on": [1]},
    {"step": 3, "action": "analyze_recent_deployments", "agent": "executor", "depends_on": [1]},
    {"step": 4, "action": "identify_root_cause", "agent": "planner", "depends_on": [2, 3]},
    {"step": 5, "action": "challenge_hypothesis", "agent": "critic", "depends_on": [4]},
    {"step": 6, "action": "verify_remediation", "agent": "verification", "depends_on": [5]}
  ]
}
```

### Executor Agent

**Allowed Operations:** `kubectl get/describe/logs`, `git log/diff/show`, read-only API calls

**NOT Allowed (without human approval):** `kubectl delete/apply`, `git push`, any write operations

**Execution Environment:** Sandboxed containers (Docker) or Firecracker microVMs

### Critic Agent — Challenges assumptions, detects hallucinations, requests additional evidence

### Verification Agent — Validates analyses with tests, metrics, and confidence scoring

### Reflection Agent — Learns from completed workflows, updates long-term memory

---

# 9. Event-Driven Architecture

### Core Event Schema (Protobuf — Shared Across Java & Python)

```protobuf
// proto/aegisops/events.proto
syntax = "proto3";
package aegisops.events;

message AegisEvent {
    string event_id = 1;
    string event_type = 2;           // "ci.build.failed", "k8s.pod.crashloop"
    google.protobuf.Timestamp timestamp = 3;
    string source = 4;               // "github_actions", "kubernetes"
    string tenant_id = 5;
    google.protobuf.Struct payload = 6;
    map<string, string> metadata = 7;
}

message WorkflowEvent {
    string workflow_id = 1;
    string event_type = 2;           // "step.started", "agent.thought", "workflow.completed"
    google.protobuf.Timestamp timestamp = 3;
    google.protobuf.Struct data = 4;
}
```

### Core Event Types

| Event | Source | Trigger |
|---|---|---|
| `ci.build.failed` | GitHub Actions / Jenkins | Build or test failure |
| `deployment.failed` | ArgoCD / Spinnaker | Deployment rollout failure |
| `deployment.completed` | ArgoCD / Spinnaker | Successful deployment |
| `k8s.pod.crashloop` | Kubernetes | Pod in CrashLoopBackOff |
| `k8s.pod.oom` | Kubernetes | Out-of-memory kill |
| `metric.latency.spike` | Prometheus | P99 latency exceeds threshold |
| `incident.created` | PagerDuty / OpsGenie | New incident opened |
| `agent.plan.generated` | Agent Runtime (Python) | Investigation plan created |
| `verification.completed` | Verification Service (Java) | Verification passed/failed |
| `human.approved` | Dashboard | Human approved remediation |

### Event Flow

```
External Webhook → Spring Boot Ingestion → Kafka Topic
    ↓                                           ↓
Signature Validation                  Spring Boot Workflow Orchestrator
Event Parsing                         (Creates Temporal Workflow)
Deduplication (Redis)                       ↓
                                     Publish task to Kafka → Python Agent Runtime
                                                                  ↓
                                                            LangGraph Execution
                                                                  ↓
                                                            Kafka result → Spring Boot
                                                                  ↓
                                                            Verification (Java)
                                                                  ↓
                                                            Human Approval (if needed)
                                                                  ↓
                                                            Resolution + Memory Update
```

---

# 10. Memory System Design

### Three-Tier Memory Architecture (Python Services)

```
┌─────────────────────────────────────────┐
│         Short-Term Memory (Redis)        │
│  TTL: 24 hours                          │
│  Contents: Active workflow context,      │
│  current agent conversation, temp data   │
├─────────────────────────────────────────┤
│         Long-Term Memory (PostgreSQL)    │
│  TTL: Permanent                         │
│  Contents: Historical incidents,         │
│  resolutions, patterns, team knowledge   │
├─────────────────────────────────────────┤
│    Semantic Memory (pgvector)            │
│  TTL: Permanent                         │
│  Contents: Embeddings for semantic       │
│  search across all stored memories       │
└─────────────────────────────────────────┘
```

### Memory Compression Strategies

| Strategy | When Applied | How |
|---|---|---|
| **Log Deduplication** | Before storage | Remove duplicate log lines, keep counts |
| **Semantic Summarization** | After workflow completes | LLM summarizes investigation into key findings |
| **Token-Aware Pruning** | Before agent context construction | Trim oldest/least-relevant context to fit token window |
| **Hierarchical Compression** | Periodically | Raw logs → summaries → key facts → metadata |

---

# 11. RAG Pipeline

### Data Source Ingestion (Python Retrieval Service)

| Source | Chunking Strategy | Update Frequency |
|---|---|---|
| GitHub Repos | By function/class/module (AST-aware) | On push webhook |
| Runbooks | By section/step | On update webhook |
| Jira Tickets | By ticket (title + description + comments) | Hourly sync |
| Slack Messages | By thread | Daily sync |
| Architecture Docs | By paragraph/section | On update webhook |
| Postmortems | By incident (full document) | On creation |
| Logs | By error window / trace ID / time range | Real-time streaming |

### Retrieval Pipeline

```
User Query / Agent Query
        ↓
    Query Expansion (LLM rewrites ambiguous queries)
        ↓
    ┌──────────────────────────────────┐
    │    Parallel Retrieval            │
    │                                  │
    │  Vector Search (pgvector)        │
    │  Keyword Search (PostgreSQL FTS) │
    │  Metadata Filter (time, team)    │
    └──────────────┬───────────────────┘
                   ↓
         Reciprocal Rank Fusion (combine results)
                   ↓
         Cross-Encoder Re-ranking (relevance scoring)
                   ↓
         Context Compression (fit token budget)
                   ↓
         Agent Context Window
```

---

# 12. Backend Engineering

### Communication Patterns

| Pattern | Use Case | Technology |
|---|---|---|
| **REST** | External API (Dashboard ↔ Backend) | Spring Boot REST Controllers |
| **gRPC** | Java ↔ Python internal calls | Protobuf contracts (shared `.proto` files) |
| **Kafka** | Async event processing | Spring Kafka (Java) + aiokafka (Python) |
| **WebSocket** | Real-time dashboard streaming | Spring WebSocket (STOMP) |
| **SSE** | One-way streaming (agent logs) | Python FastAPI StreamingResponse |

### Shared Protobuf Contracts

```
proto/
├── aegisops/
│   ├── events.proto          # Event schemas (used by all services)
│   ├── retrieval.proto       # Retrieval service gRPC contract
│   ├── agent.proto           # Agent task/result schemas
│   ├── memory.proto          # Memory service gRPC contract
│   └── common.proto          # Shared types (Severity, Status, etc.)
```

Both Java and Python services generate code from these `.proto` files:
- **Java:** `protobuf-maven-plugin` generates Java classes
- **Python:** `grpcio-tools` generates Python stubs

### Circuit Breaker Pattern (Spring Boot — Resilience4j)

```java
@Service
public class LLMGatewayService {

    @CircuitBreaker(name = "llm-provider", fallbackMethod = "fallbackLLMCall")
    @Retry(name = "llm-provider")
    @RateLimiter(name = "llm-provider")
    public String callLLM(String prompt, String model) {
        return llmClient.generate(prompt, model);
    }

    public String fallbackLLMCall(String prompt, String model, Throwable t) {
        log.warn("LLM provider {} failed, trying fallback", model, t);
        return llmClient.generate(prompt, FALLBACK_MODEL);
    }
}
```

```yaml
# application.yml — Resilience4j config
resilience4j:
  circuitbreaker:
    instances:
      llm-provider:
        failureRateThreshold: 50
        waitDurationInOpenState: 60s
        slidingWindowSize: 10
  retry:
    instances:
      llm-provider:
        maxAttempts: 3
        waitDuration: 2s
        exponentialBackoffMultiplier: 2
```

### Saga Pattern (Temporal — Java)

For multi-step remediation workflows where partial failure requires compensation:

```java
@WorkflowImpl(taskQueues = "remediation")
public class RemediationSagaWorkflow implements RemediationWorkflow {

    @Override
    public RemediationResult execute(RemediationPlan plan) {
        Saga saga = new Saga(new Saga.Options.Builder().build());
        try {
            // Step 1: Create Jira ticket
            String ticketId = activities.createJiraTicket(plan);
            saga.addCompensation(activities::deleteJiraTicket, ticketId);
            
            // Step 2: Create PR with fix
            String prUrl = activities.createPullRequest(plan);
            saga.addCompensation(activities::closePullRequest, prUrl);
            
            // Step 3: Run tests on PR
            activities.runTests(prUrl);
            
            // Step 4: Deploy to staging
            String deploymentId = activities.deployToStaging(plan);
            saga.addCompensation(activities::rollbackStaging, deploymentId);
            
            // Step 5: Canary verification
            activities.runCanaryVerification(deploymentId);
            
            return RemediationResult.success();
        } catch (Exception e) {
            saga.compensate();  // Undo all completed steps in reverse order
            throw e;
        }
    }
}
```

---

# 13. Database Design

### PostgreSQL Schema (Accessed by Both Java and Python Services)

```sql
-- Core workflow tracking
CREATE TABLE workflows (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       UUID NOT NULL REFERENCES tenants(id),
    trigger_event_id UUID,
    workflow_type   VARCHAR(50) NOT NULL,
    status          VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    priority        INTEGER DEFAULT 3,
    created_at      TIMESTAMPTZ DEFAULT NOW(),
    updated_at      TIMESTAMPTZ DEFAULT NOW(),
    completed_at    TIMESTAMPTZ,
    result          JSONB,
    metadata        JSONB DEFAULT '{}'::JSONB
);

CREATE INDEX idx_workflows_tenant_status ON workflows(tenant_id, status);
CREATE INDEX idx_workflows_created ON workflows(created_at DESC);

-- Individual steps within a workflow
CREATE TABLE workflow_steps (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    workflow_id     UUID NOT NULL REFERENCES workflows(id) ON DELETE CASCADE,
    step_order      INTEGER NOT NULL,
    step_type       VARCHAR(50) NOT NULL,
    agent_name      VARCHAR(50),
    status          VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    input           JSONB,
    output          JSONB,
    started_at      TIMESTAMPTZ,
    completed_at    TIMESTAMPTZ,
    error           TEXT,
    retry_count     INTEGER DEFAULT 0
);

-- Incident tracking
CREATE TABLE incidents (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       UUID NOT NULL REFERENCES tenants(id),
    external_id     VARCHAR(255),
    severity        VARCHAR(10) NOT NULL,
    title           TEXT NOT NULL,
    service_name    VARCHAR(255) NOT NULL,
    root_cause      TEXT,
    resolution      TEXT,
    status          VARCHAR(20) DEFAULT 'OPEN',
    created_at      TIMESTAMPTZ DEFAULT NOW(),
    resolved_at     TIMESTAMPTZ,
    mttr_seconds    INTEGER,
    tags            TEXT[],
    metadata        JSONB DEFAULT '{}'::JSONB
);

-- Audit log (append-only)
CREATE TABLE audit_logs (
    id              BIGSERIAL PRIMARY KEY,
    tenant_id       UUID NOT NULL,
    user_id         UUID,
    agent_name      VARCHAR(50),
    action          VARCHAR(100) NOT NULL,
    resource_type   VARCHAR(50),
    resource_id     UUID,
    details         JSONB,
    ip_address      INET,
    timestamp       TIMESTAMPTZ DEFAULT NOW()
);

-- Multi-tenant support
CREATE TABLE tenants (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name            VARCHAR(255) NOT NULL,
    slug            VARCHAR(100) UNIQUE NOT NULL,
    plan            VARCHAR(20) DEFAULT 'free',
    settings        JSONB DEFAULT '{}'::JSONB,
    created_at      TIMESTAMPTZ DEFAULT NOW()
);

-- Vector embeddings (accessed by Python services)
CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE embeddings (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       UUID NOT NULL REFERENCES tenants(id),
    source_type     VARCHAR(50) NOT NULL,
    source_id       VARCHAR(255) NOT NULL,
    chunk_text      TEXT NOT NULL,
    embedding       vector(1536) NOT NULL,
    metadata        JSONB DEFAULT '{}'::JSONB,
    created_at      TIMESTAMPTZ DEFAULT NOW()
);

CREATE INDEX idx_embeddings_vector ON embeddings 
    USING ivfflat (embedding vector_cosine_ops) WITH (lists = 100);
```

### Database Access Pattern

| Service | Language | DB Access | ORM/Client |
|---|---|---|---|
| Workflow Orchestrator | Java | workflows, workflow_steps | Spring Data JPA / Hibernate |
| Auth Service | Java | tenants, audit_logs | Spring Data JPA |
| Ingestion Service | Java | events | Spring Data JPA |
| Verification Service | Java | workflows (read) | Spring Data JPA |
| Agent Runtime | Python | workflow_steps (write results) | asyncpg (raw SQL) |
| Retrieval Service | Python | embeddings | asyncpg + pgvector |
| Memory Service | Python | embeddings, incidents (read) | asyncpg + pgvector |

### Redis Usage

| Purpose | Key Pattern | TTL | Accessed By |
|---|---|---|---|
| Session Memory | `session:{workflow_id}` | 24 hours | Python (Memory Service) |
| Rate Limiting | `ratelimit:{user_id}:{endpoint}` | 1 minute | Java (API Gateway) |
| Cache | `cache:{query_hash}` | 1 hour | Python (Retrieval) |
| Agent Locks | `lock:agent:{agent_id}` | 5 minutes | Python (Agent Runtime) |
| Event Dedup | `dedup:{event_hash}` | 1 hour | Java (Ingestion) |
| JWT Blacklist | `jwt:blacklist:{jti}` | Token TTL | Java (Auth Service) |

---

# 14. Queue & Workflow Systems

### Kafka Topic Architecture

| Topic | Producer | Consumer | Purpose |
|---|---|---|---|
| `aegisops.events.raw` | Ingestion (Java) | Workflow Orchestrator (Java) | Raw external events |
| `agents.plan` | Orchestrator (Java) | Agent Runtime (Python) | Planning tasks |
| `agents.execute` | Orchestrator (Java) | Agent Runtime (Python) | Agent execution tasks |
| `agents.results` | Agent Runtime (Python) | Orchestrator (Java) | Agent results |
| `retrieval.index` | Ingestion (Java) | Retrieval (Python) | Documents to index |
| `workflow.events` | Orchestrator (Java) | Notification (Java) | Workflow lifecycle events |
| `verification.requests` | Orchestrator (Java) | Verification (Java) | Verification tasks |

### Workflow State Machine

```
PENDING → RUNNING → WAITING_APPROVAL → VERIFYING → COMPLETED
                ↘                              ↗
                  → FAILED → RETRY → RUNNING
                       ↘
                        → DEAD_LETTER
                ↘
                  → ROLLED_BACK
```

### Kafka Configuration (Spring Boot)

```java
@Configuration
public class KafkaConfig {

    @Bean
    public ProducerFactory<String, AegisEvent> producerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        config.put(ProducerConfig.ACKS_CONFIG, "all");  // Durability
        config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);  // Exactly-once
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, AegisEvent> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
```

### Dead Letter Queue (Spring Boot)

```java
@KafkaListener(topics = "aegisops.events.raw")
@RetryableTopic(
    attempts = "3",
    backoff = @Backoff(delay = 2000, multiplier = 2),
    dltTopicSuffix = ".dlt",
    autoCreateTopics = "true"
)
public void processEvent(AegisEvent event) {
    workflowService.createWorkflow(event);
}

@DltHandler
public void handleDeadLetter(AegisEvent event) {
    log.error("Event failed after retries: {}", event.getEventId());
    auditService.logDeadLetter(event);
    slackNotifier.alertOnCallEngineer(event);
}
```

---

# 15. Authentication & RBAC

### OAuth2/OIDC Flow (Spring Security)

```java
@Configuration
public class OAuth2Config {

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(
            googleRegistration(),
            githubRegistration()
        );
    }

    private ClientRegistration googleRegistration() {
        return ClientRegistration.withRegistrationId("google")
            .clientId("${GOOGLE_CLIENT_ID}")
            .clientSecret("${GOOGLE_CLIENT_SECRET}")
            .scope("openid", "profile", "email")
            .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
            .tokenUri("https://oauth2.googleapis.com/token")
            .userInfoUri("https://openidconnect.googleapis.com/v1/userinfo")
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
            .build();
    }
}
```

### JWT Token Structure

```json
{
  "sub": "user_123",
  "tenant_id": "tenant_001",
  "roles": ["SRE"],
  "permissions": ["workflow:create", "workflow:approve", "incident:read"],
  "iat": 1705312200,
  "exp": 1705313100
}
```

### API Key Authentication (for webhook integrations)

```java
@Component
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                     HttpServletResponse response,
                                     FilterChain filterChain) throws IOException, ServletException {
        String apiKey = request.getHeader("X-API-Key");
        if (apiKey != null) {
            String keyHash = DigestUtils.sha256Hex(apiKey);
            ServiceAccount account = apiKeyRepository.findByKeyHash(keyHash)
                .orElseThrow(() -> new AuthenticationException("Invalid API key"));
            
            if (account.getExpiresAt().isBefore(Instant.now())) {
                throw new AuthenticationException("API key expired");
            }
            
            SecurityContextHolder.getContext().setAuthentication(
                new ApiKeyAuthentication(account)
            );
        }
        filterChain.doFilter(request, response);
    }
}
```

---

# 16. Security Architecture

### Zero Trust Principles

1. **Every service authenticates every request** — no implicit trust between services
2. **mTLS for all internal communication** — service mesh (Istio/Linkerd)
3. **Principle of least privilege** — agents only get tools they need
4. **All secrets in Vault/KMS** — never in environment variables or code

### Sandboxed Execution (Python Agent Runtime)

```
┌──────────────────────────────┐
│     Executor Agent (Python)  │
│                              │
│  ┌────────────────────────┐  │
│  │  Docker Container      │  │
│  │  ┌──────────────────┐  │  │
│  │  │  Read-only tools  │  │  │
│  │  │  - kubectl get    │  │  │
│  │  │  - git log        │  │  │
│  │  │  - curl (limited) │  │  │
│  │  └──────────────────┘  │  │
│  │  Network: restricted   │  │
│  │  Filesystem: read-only │  │
│  │  CPU: 0.5 cores max   │  │
│  │  Memory: 256MB max    │  │
│  │  Timeout: 60 seconds  │  │
│  └────────────────────────┘  │
└──────────────────────────────┘
```

### Audit Logging (Spring Boot)

```java
@Aspect
@Component
public class AuditAspect {

    @Around("@annotation(Audited)")
    public Object auditAction(ProceedingJoinPoint joinPoint) throws Throwable {
        AuditLog log = AuditLog.builder()
            .tenantId(SecurityContext.getTenantId())
            .userId(SecurityContext.getUserId())
            .action(joinPoint.getSignature().getName())
            .timestamp(Instant.now())
            .build();
        
        try {
            Object result = joinPoint.proceed();
            log.setResult("SUCCESS");
            auditRepository.save(log);
            return result;
        } catch (Exception e) {
            log.setResult("FAILURE");
            log.setDetails(Map.of("error", e.getMessage()));
            auditRepository.save(log);
            throw e;
        }
    }
}
```

---

# 17. Observability Stack

### Technology Choices

| Component | Technology | Integration |
|---|---|---|
| **Metrics** | Prometheus + Grafana | Micrometer (Java), prometheus_client (Python) |
| **Logs** | Loki + Grafana | Logback/SLF4J (Java), structlog (Python) |
| **Traces** | Tempo + Grafana | Micrometer Tracing (Java), opentelemetry-python |
| **Instrumentation** | OpenTelemetry | Both Java and Python emit OTel traces |

### Spring Boot Observability Config

```yaml
# application.yml
management:
  endpoints:
    web:
      exposure:
        include: health,metrics,prometheus
  tracing:
    sampling:
      probability: 1.0
  otlp:
    tracing:
      endpoint: http://tempo:4318/v1/traces

spring:
  application:
    name: workflow-orchestrator
```

### Key Dashboards

1. **Workflow Overview** — active workflows, success rate, avg duration
2. **Agent Performance** — per-agent latency, token usage, accuracy
3. **Infrastructure** — pod health, resource utilization, queue depth
4. **Incident Analytics** — MTTR trends, incident volume, root cause categories
5. **Cost Dashboard** — LLM token costs, compute costs, storage costs

### Critical Alerts

```yaml
groups:
  - name: aegisops-critical
    rules:
      - alert: HighWorkflowFailureRate
        expr: rate(workflow_failures_total[5m]) / rate(workflow_total[5m]) > 0.2
        for: 5m
        labels:
          severity: critical
          
      - alert: AgentLatencyHigh
        expr: histogram_quantile(0.99, agent_execution_duration_seconds_bucket) > 120
        for: 10m
        labels:
          severity: warning
          
      - alert: KafkaConsumerLag
        expr: kafka_consumer_group_lag > 1000
        for: 5m
        labels:
          severity: critical
```

---

# 18. Real-Time Streaming System

### Spring WebSocket (STOMP) Architecture

```java
// Client subscribes to workflow updates
// SUBSCRIBE /topic/workflows/{workflowId}
// Server pushes events as they happen

@Service
public class WorkflowStreamService {

    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = "workflow.events")
    public void broadcastWorkflowEvent(WorkflowEvent event) {
        messagingTemplate.convertAndSend(
            "/topic/workflows/" + event.getWorkflowId(),
            WorkflowEventDTO.from(event)
        );
    }
}
```

### Streaming Event Types

| Event | Payload | Source |
|---|---|---|
| `workflow.started` | Workflow ID, type, priority | Orchestrator (Java) |
| `step.started` | Step name, agent name | Orchestrator (Java) |
| `agent.thought` | Reasoning text | Agent Runtime (Python → Kafka) |
| `agent.tool_call` | Tool name, args, result | Agent Runtime (Python → Kafka) |
| `step.completed` | Step result, duration | Orchestrator (Java) |
| `verification.result` | Pass/fail, details | Verification (Java) |
| `workflow.completed` | Final result, MTTR | Orchestrator (Java) |

---

# 19. Deployment Architecture

### Kubernetes Namespace Layout

```
aegisops-frontend/       → Dashboard, static assets
aegisops-java/           → All Spring Boot services (API, Auth, Orchestrator, etc.)
aegisops-python/         → All Python services (Agent Runtime, Retrieval, Memory)
aegisops-infra/          → Kafka, Redis, PostgreSQL, Temporal
aegisops-observability/  → Prometheus, Grafana, Loki, Tempo
```

### Resource Allocation Strategy

| Service | Language | CPU Req | CPU Limit | Mem Req | Mem Limit | Replicas |
|---|---|---|---|---|---|---|
| API Gateway | Java | 500m | 1000m | 512Mi | 1Gi | 2-4 |
| Auth Service | Java | 250m | 500m | 256Mi | 512Mi | 2 |
| Workflow Orchestrator | Java | 500m | 1000m | 512Mi | 1Gi | 2-3 |
| Ingestion Service | Java | 250m | 500m | 256Mi | 512Mi | 2-4 |
| Notification Service | Java | 250m | 500m | 256Mi | 512Mi | 2 |
| Verification Service | Java | 250m | 500m | 256Mi | 512Mi | 2-4 |
| Agent Runtime | Python | 1000m | 2000m | 1Gi | 2Gi | 2-8 (HPA) |
| Retrieval Service | Python | 500m | 1000m | 1Gi | 2Gi | 2-4 |
| Memory Service | Python | 250m | 500m | 256Mi | 512Mi | 2 |
| Embedding Workers | Python | 500m | 1000m | 512Mi | 1Gi | 1-4 (HPA) |

---

# 20. Kubernetes Infrastructure

### Node Pools

| Pool | Instance Type | Purpose | Scaling |
|---|---|---|---|
| **system** | `e2-standard-4` | K8s system, observability | Fixed (2 nodes) |
| **java-services** | `e2-standard-8` | All Spring Boot services | Auto (2-8 nodes) |
| **python-ai** | `e2-standard-8` | Agent runtime, retrieval, embedding | Auto (2-6 nodes) |
| **gpu-inference** | `n1-standard-4` + T4 | Local model inference (optional) | Auto (0-4 nodes) |

### Helm Chart Structure

```
infra/helm/
├── aegisops/
│   ├── Chart.yaml
│   ├── values.yaml
│   ├── values-staging.yaml
│   ├── values-production.yaml
│   └── templates/
│       ├── java-services/
│       │   ├── api-gateway.yaml
│       │   ├── auth-service.yaml
│       │   ├── workflow-orchestrator.yaml
│       │   ├── ingestion-service.yaml
│       │   ├── notification-service.yaml
│       │   └── verification-service.yaml
│       ├── python-services/
│       │   ├── agent-runtime.yaml
│       │   ├── retrieval-service.yaml
│       │   ├── memory-service.yaml
│       │   └── embedding-workers.yaml
│       └── shared/
│           ├── configmap.yaml
│           ├── secret.yaml
│           └── networkpolicy.yaml
```

---

# 21. CI/CD Pipeline

### Pipeline Stages (Separate for Java and Python)

```
┌──────────────────────────────────────────────────┐
│                   Java Pipeline                    │
│  Lint → Compile → Unit Test → Integration Test    │
│  → Spotbugs → Docker Build → Deploy               │
└──────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────┐
│                  Python Pipeline                   │
│  Ruff → Mypy → Pytest → Docker Build → Deploy    │
└──────────────────────────────────────────────────┘

        ↓ Both pipelines complete ↓

┌──────────────────────────────────────────────────┐
│              Integration Pipeline                  │
│  E2E Tests → Canary Deploy → Production           │
└──────────────────────────────────────────────────┘
```

### GitHub Actions Workflow

```yaml
name: AEGISOPS CI/CD
on:
  push:
    branches: [main, develop]
  pull_request:
    branches: [main]

jobs:
  # ============ JAVA SERVICES ============
  java-build-test:
    runs-on: ubuntu-latest
    services:
      postgres:
        image: pgvector/pgvector:pg16
        env:
          POSTGRES_DB: aegisops_test
          POSTGRES_PASSWORD: test
        ports: [5432:5432]
      redis:
        image: redis:7
        ports: [6379:6379]
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          distribution: 'temurin'
          java-version: '21'
          cache: 'maven'
      - run: cd java-services && mvn verify -P ci
      - run: cd java-services && mvn spotbugs:check
      - uses: codecov/codecov-action@v4

  # ============ PYTHON SERVICES ============
  python-build-test:
    runs-on: ubuntu-latest
    services:
      postgres:
        image: pgvector/pgvector:pg16
        env:
          POSTGRES_DB: aegisops_test
          POSTGRES_PASSWORD: test
        ports: [5432:5432]
      redis:
        image: redis:7
        ports: [6379:6379]
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-python@v5
        with:
          python-version: '3.12'
      - run: cd python-services && pip install -r requirements.txt -r requirements-dev.txt
      - run: cd python-services && ruff check .
      - run: cd python-services && mypy --strict .
      - run: cd python-services && pytest tests/ -v --cov --cov-report=xml
      - uses: codecov/codecov-action@v4

  # ============ PROTO VALIDATION ============
  proto-check:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: bufbuild/buf-setup-action@v1
      - run: buf lint proto/
      - run: buf breaking proto/ --against '.git#branch=main'

  # ============ DOCKER BUILD ============
  build-and-push:
    needs: [java-build-test, python-build-test, proto-check]
    if: github.ref == 'refs/heads/main'
    runs-on: ubuntu-latest
    strategy:
      matrix:
        service:
          - { name: api-gateway, path: java-services/api-gateway }
          - { name: auth-service, path: java-services/auth-service }
          - { name: workflow-orchestrator, path: java-services/workflow-orchestrator }
          - { name: agent-runtime, path: python-services/agent-runtime }
          - { name: retrieval-service, path: python-services/retrieval-service }
    steps:
      - uses: actions/checkout@v4
      - uses: docker/build-push-action@v5
        with:
          context: ${{ matrix.service.path }}
          push: true
          tags: ghcr.io/myorg/aegisops-${{ matrix.service.name }}:${{ github.sha }}
```

---

# 22. AI Evaluation System

### Evaluation Metrics

| Metric | Description | Target |
|---|---|---|
| **Planning Accuracy** | % of investigation plans that lead to correct root cause | > 80% |
| **Retrieval Precision@5** | % of top-5 retrieved documents that are relevant | > 75% |
| **Hallucination Rate** | % of agent outputs containing fabricated information | < 5% |
| **Verification Success Rate** | % of verifications that correctly confirm/deny hypotheses | > 90% |
| **Human Override Rate** | % of agent decisions overridden by humans | < 15% |
| **MTTR Improvement** | Reduction in mean time to resolution vs. baseline | > 50% |

---

# 23. Cost Optimization

### Model Routing Strategy (Python Agent Runtime)

```python
class ModelRouter:
    ROUTING_TABLE = {
        "log_summarization":     "gemini-2.0-flash",     # Simple, fast
        "query_expansion":       "gemini-2.0-flash",     # Simple NLU
        "incident_analysis":     "gemini-2.5-pro",       # Complex reasoning
        "root_cause_reasoning":  "gemini-2.5-pro",       # Complex reasoning
        "remediation_planning":  "claude-sonnet-4",      # Critical decisions
        "reflection":            "gemini-2.0-flash",     # Pattern extraction
    }
```

### Cost Tracking (Spring Boot)

```java
@Service
public class CostTracker {
    
    @KafkaListener(topics = "agents.results")
    public void trackAgentCost(AgentResult result) {
        CostRecord record = CostRecord.builder()
            .tenantId(result.getTenantId())
            .model(result.getModelUsed())
            .inputTokens(result.getInputTokens())
            .outputTokens(result.getOutputTokens())
            .cost(calculateCost(result))
            .timestamp(Instant.now())
            .build();
        costRepository.save(record);
    }
}
```

---

# 24. Folder Structure

```
aegisops/
│
├── proto/                               # Shared Protobuf contracts
│   └── aegisops/
│       ├── events.proto
│       ├── retrieval.proto
│       ├── agent.proto
│       ├── memory.proto
│       └── common.proto
│
├── java-services/                       # Spring Boot services (Java 21)
│   ├── pom.xml                          # Parent POM (multi-module Maven)
│   │
│   ├── common/                          # Shared Java code
│   │   ├── pom.xml
│   │   └── src/main/java/
│   │       └── com/aegisops/common/
│   │           ├── models/              # Shared DTOs, entities
│   │           ├── security/            # Auth utilities
│   │           ├── kafka/               # Kafka config & serializers
│   │           └── observability/       # Metrics, tracing helpers
│   │
│   ├── api-gateway/
│   │   ├── pom.xml
│   │   ├── src/main/java/.../gateway/
│   │   │   ├── GatewayApplication.java
│   │   │   ├── config/
│   │   │   │   ├── RoutesConfig.java
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   └── RateLimitConfig.java
│   │   │   └── filters/
│   │   ├── src/main/resources/
│   │   │   └── application.yml
│   │   └── Dockerfile
│   │
│   ├── auth-service/
│   │   ├── pom.xml
│   │   ├── src/main/java/.../auth/
│   │   │   ├── AuthApplication.java
│   │   │   ├── config/SecurityConfig.java
│   │   │   ├── controller/AuthController.java
│   │   │   ├── service/
│   │   │   │   ├── JwtService.java
│   │   │   │   ├── RbacService.java
│   │   │   │   └── ApiKeyService.java
│   │   │   └── repository/
│   │   └── Dockerfile
│   │
│   ├── workflow-orchestrator/
│   │   ├── pom.xml
│   │   ├── src/main/java/.../orchestrator/
│   │   │   ├── OrchestratorApplication.java
│   │   │   ├── workflows/
│   │   │   │   ├── IncidentInvestigationWorkflow.java
│   │   │   │   ├── BuildAnalysisWorkflow.java
│   │   │   │   └── DeploymentVerificationWorkflow.java
│   │   │   ├── activities/
│   │   │   │   ├── PlanningActivity.java
│   │   │   │   ├── RetrievalActivity.java      # gRPC call to Python
│   │   │   │   ├── AgentExecutionActivity.java  # Kafka to Python
│   │   │   │   └── VerificationActivity.java
│   │   │   ├── kafka/
│   │   │   │   ├── EventConsumer.java
│   │   │   │   └── ResultConsumer.java
│   │   │   └── temporal/
│   │   │       └── TemporalWorkerConfig.java
│   │   └── Dockerfile
│   │
│   ├── ingestion-service/
│   │   ├── pom.xml
│   │   ├── src/main/java/.../ingestion/
│   │   │   ├── IngestionApplication.java
│   │   │   ├── controller/WebhookController.java
│   │   │   ├── parsers/
│   │   │   │   ├── GitHubEventParser.java
│   │   │   │   ├── PagerDutyEventParser.java
│   │   │   │   └── AlertManagerEventParser.java
│   │   │   ├── service/EventDeduplicationService.java
│   │   │   └── kafka/EventPublisher.java
│   │   └── Dockerfile
│   │
│   ├── notification-service/
│   │   ├── pom.xml
│   │   ├── src/main/java/.../notification/
│   │   │   ├── NotificationApplication.java
│   │   │   ├── config/WebSocketConfig.java
│   │   │   ├── service/
│   │   │   │   ├── SlackNotifier.java
│   │   │   │   ├── EmailNotifier.java
│   │   │   │   └── WorkflowStreamService.java
│   │   │   └── kafka/NotificationConsumer.java
│   │   └── Dockerfile
│   │
│   └── verification-service/
│       ├── pom.xml
│       ├── src/main/java/.../verification/
│       │   ├── VerificationApplication.java
│       │   ├── service/
│       │   │   ├── VerificationService.java
│       │   │   ├── SafetyScorer.java
│       │   │   └── CanaryAnalyzer.java
│       │   └── controller/VerificationController.java
│       └── Dockerfile
│
├── python-services/                     # Python AI services
│   ├── requirements.txt
│   ├── requirements-dev.txt
│   ├── pyproject.toml
│   │
│   ├── agent-runtime/
│   │   ├── agents/
│   │   │   ├── supervisor.py
│   │   │   ├── planner.py
│   │   │   ├── retrieval_agent.py
│   │   │   ├── executor.py
│   │   │   ├── critic.py
│   │   │   ├── verification_agent.py
│   │   │   └── reflection.py
│   │   ├── tools/
│   │   │   ├── kubectl_tool.py
│   │   │   ├── github_tool.py
│   │   │   ├── metrics_tool.py
│   │   │   └── jira_tool.py
│   │   ├── graph.py                     # LangGraph agent graph
│   │   ├── kafka_consumer.py            # Receives tasks from Java
│   │   ├── kafka_producer.py            # Sends results to Java
│   │   ├── main.py                      # FastAPI app
│   │   └── Dockerfile
│   │
│   ├── retrieval-service/
│   │   ├── retrieval_service.py
│   │   ├── embeddings.py
│   │   ├── hybrid_search.py
│   │   ├── reranker.py
│   │   ├── chunkers/
│   │   │   ├── code_chunker.py
│   │   │   ├── log_chunker.py
│   │   │   └── doc_chunker.py
│   │   ├── grpc_server.py               # gRPC server (called by Java)
│   │   ├── main.py
│   │   └── Dockerfile
│   │
│   ├── memory-service/
│   │   ├── memory_service.py
│   │   ├── short_term.py
│   │   ├── long_term.py
│   │   ├── compression.py
│   │   ├── main.py
│   │   └── Dockerfile
│   │
│   └── embedding-workers/
│       ├── worker.py                    # Kafka consumer for batch embedding
│       ├── embeddings.py
│       └── Dockerfile
│
├── frontend/                            # React/Next.js dashboard
│   ├── src/
│   │   ├── components/
│   │   ├── pages/
│   │   ├── hooks/
│   │   └── services/
│   ├── package.json
│   └── Dockerfile
│
├── prompts/                             # AI agent prompts (version controlled)
│   ├── supervisor_system.md
│   ├── planner_system.md
│   ├── critic_system.md
│   └── verification_system.md
│
├── evaluation/                          # AI evaluation & benchmarks
│   ├── benchmarks/
│   ├── eval_pipeline.py
│   └── metrics.py
│
├── infra/                               # Infrastructure as Code
│   ├── terraform/
│   ├── kubernetes/
│   └── helm/
│
├── observability/
│   ├── prometheus/
│   ├── grafana/
│   ├── loki/
│   └── tempo/
│
├── docs/
│   ├── architecture.md
│   ├── api-reference.md
│   ├── deployment-guide.md
│   └── contributing.md
│
├── scripts/
│   ├── setup-dev.sh
│   ├── run-migrations.sh
│   └── load-test.py
│
├── migrations/                          # Flyway/Alembic migrations
│   ├── V001__initial_schema.sql
│   ├── V002__add_vector_extension.sql
│   └── V003__add_audit_logs.sql
│
├── docker-compose.yml                   # Local development
├── docker-compose.dev.yml
├── Makefile
├── .env.example
├── .gitignore
└── README.md
```

---

# 25. API Design

### REST API Endpoints (Spring Boot Controllers)

```http
POST   /api/v1/workflows                    # Create new investigation
GET    /api/v1/workflows                    # List workflows (paginated, filtered)
GET    /api/v1/workflows/{id}               # Get workflow details
GET    /api/v1/workflows/{id}/steps         # Get workflow steps
POST   /api/v1/workflows/{id}/approve       # Approve remediation
POST   /api/v1/workflows/{id}/reject        # Reject remediation
DELETE /api/v1/workflows/{id}               # Cancel workflow

POST   /api/v1/incidents                    # Create/report incident
GET    /api/v1/incidents                    # List incidents
GET    /api/v1/incidents/{id}               # Get incident details
GET    /api/v1/incidents/{id}/similar       # Find similar (proxied to Python)

POST   /api/v1/webhooks/github             # GitHub webhook
POST   /api/v1/webhooks/pagerduty          # PagerDuty webhook
POST   /api/v1/webhooks/alertmanager       # Alertmanager webhook

GET    /api/v1/dashboard/stats             # Overview statistics
GET    /api/v1/analytics/mttr              # MTTR analysis
GET    /api/v1/analytics/costs             # Cost breakdown
```

### WebSocket (STOMP)

```
SUBSCRIBE /topic/workflows/{workflowId}     # Stream workflow events
SUBSCRIBE /topic/dashboard                  # Stream dashboard updates
```

### gRPC (Internal — Java ↔ Python)

```protobuf
// proto/aegisops/retrieval.proto
service RetrievalService {
    rpc Search (SearchRequest) returns (SearchResponse);
    rpc IndexDocument (IndexRequest) returns (IndexResponse);
}

// proto/aegisops/memory.proto
service MemoryService {
    rpc RecallSimilar (RecallRequest) returns (RecallResponse);
    rpc StoreResolution (StoreRequest) returns (StoreResponse);
}
```

---

# 26. Scaling Considerations

### Horizontal Scaling

| Service | Language | Scaling Metric | Scale Range |
|---|---|---|---|
| API Gateway | Java | Request rate | 2 → 10 pods |
| Workflow Orchestrator | Java | Temporal workflow count | 2 → 6 pods |
| Agent Runtime | Python | Kafka consumer lag | 2 → 8 pods |
| Retrieval Service | Python | Query latency P99 | 2 → 6 pods |
| Embedding Workers | Python | Embedding queue depth | 1 → 4 pods |
| Ingestion Service | Java | Webhook rate | 2 → 6 pods |

### Performance Targets

| Metric | Target |
|---|---|
| API Latency (P50) | < 100ms |
| API Latency (P99) | < 500ms |
| Workflow Start Time | < 2 seconds |
| Investigation Duration | 2-10 minutes |
| Dashboard Load | < 1 second |
| WebSocket Latency | < 100ms |

---

# 27. Failure Scenarios & Resilience

### LLM Provider Failure (Python → Handled by Agent Runtime)

```python
FALLBACK_CHAIN = ["gemini-2.5-pro", "claude-sonnet-4", "gpt-4o", "gemini-2.0-flash"]

async def call_with_fallback(prompt: str) -> str:
    for model in FALLBACK_CHAIN:
        try:
            return await call(model, prompt)
        except (RateLimitError, ServiceUnavailableError):
            continue
    await escalate_to_human(prompt)
```

### Kafka Failure (Handled by Spring Kafka)

| Scenario | Mitigation |
|---|---|
| Broker down | Multi-broker cluster (3+), producer retries |
| Consumer crash | Consumer group rebalancing, at-least-once delivery |
| Poison pill message | `@RetryableTopic` + DLT handler (Spring Kafka) |

### Cross-Language Communication Failure

| Scenario | Mitigation |
|---|---|
| Python service down | gRPC deadline exceeded → circuit breaker opens → retry later |
| Java service down | Kafka messages queue up → Python consumers resume when Java recovers |
| Protobuf schema mismatch | CI pipeline runs `buf breaking` to catch backward-incompatible changes |

---

# 28. Enterprise Features

### Governance
- Policy engine (Spring Boot) — configurable rules
- Approval workflows — multi-level approval chains via Temporal signals
- Change management — Spring Data Envers for entity auditing

### Compliance
- Audit trails — immutable append-only PostgreSQL table
- Retention policies — Spring `@Scheduled` cleanup jobs
- SOC 2 Readiness — Spring Security, encryption at rest/transit

### Cost Analytics
- Per-team AI usage — tracked via Kafka events from Python → stored by Java
- Token spending alerts — Spring Boot scheduler checks thresholds daily

---

# 29. Future Enhancements

| Enhancement | Description | Complexity |
|---|---|---|
| **Autonomous Rollbacks** | Auto-rollback unsafe deployments via Kubernetes API | High |
| **Dynamic Tool Generation** | Generate new operational tools dynamically | High |
| **Autonomous Test Synthesis** | Create regression tests from incidents | High |
| **Knowledge Graph** | Service dependency graph (Neo4j) | Medium |
| **Self-Healing Infrastructure** | Auto-remediate common issues | High |
| **Predictive Alerting** | Predict incidents from historical patterns | High |
| **Natural Language Operations** | "What happened to the payment service?" | Medium |

---

# 30. Detailed Phase-Wise Implementation Plan

---

## Phase Overview

| Phase | Focus | Timeline | Key Tech |
|---|---|---|---|
| **Phase 1** | Foundation & Infrastructure | 5-6 weeks | Maven multi-module, Docker Compose, Flyway, Kafka, PostgreSQL |
| **Phase 2** | Core AI Agent Pipeline | 4-5 weeks | LangGraph, FastAPI, gRPC, sentence-transformers, pgvector |
| **Phase 3** | Workflow Orchestration & Dashboard | 5-6 weeks | Temporal Java SDK, Spring WebSocket, Next.js |
| **Phase 4** | Production Intelligence | 3-4 weeks | Kubernetes Java client, Spring Kafka, incident correlation |
| **Phase 5** | Autonomous Operations | 3-4 weeks | GitHub API, canary analysis, reflection agent |
| **Phase 6** | Enterprise & Hardening | 3-4 weeks | Spring Security OAuth2, multi-tenancy, Grafana, load testing |

**Total: ~23-29 weeks**

---

## Phase 1 — Foundation & Infrastructure [Spring Boot + DevOps]

### Goal
Build the project scaffolding, database schema, Kafka event bus, Spring Boot ingestion service, and basic API with auth.

### Timeline: 5-6 weeks

### Week 1: Java Project Scaffolding & Local Dev Environment

**Tasks:**
- [ ] Initialize Maven multi-module project:
  ```
  java-services/
  ├── pom.xml (parent)
  ├── common/pom.xml
  ├── api-gateway/pom.xml
  ├── auth-service/pom.xml
  ├── workflow-orchestrator/pom.xml
  ├── ingestion-service/pom.xml
  ├── notification-service/pom.xml
  └── verification-service/pom.xml
  ```
- [ ] Configure parent POM with:
  - Java 21, Spring Boot 3.3.x, Spring Cloud 2024.x
  - Shared dependencies: Spring Data JPA, Spring Kafka, Resilience4j, Micrometer, Lombok, MapStruct
- [ ] Initialize Python services directory:
  ```
  python-services/
  ├── pyproject.toml
  ├── requirements.txt
  └── requirements-dev.txt
  ```
- [ ] Create `docker-compose.yml`:
  - PostgreSQL 16 with pgvector
  - Redis 7
  - Kafka (KRaft mode — no Zookeeper)
  - Temporal server (dev mode)
- [ ] Create `Makefile`:
  ```makefile
  dev:           docker-compose up -d
  java-build:    cd java-services && mvn clean package -DskipTests
  java-test:     cd java-services && mvn verify
  python-test:   cd python-services && pytest tests/ -v
  lint:          cd java-services && mvn spotbugs:check && cd ../python-services && ruff check .
  migrate:       cd java-services && mvn flyway:migrate
  ```
- [ ] Set up shared `.proto` files directory
- [ ] Create `.env.example`, `.gitignore`, `README.md`

**Acceptance Criteria:**
- `docker-compose up` starts all infrastructure
- `mvn clean package` builds all Java modules
- All services start without errors

### Week 2: Database Schema & Migrations (Flyway)

**Tasks:**
- [ ] Set up Flyway in `common/` module
- [ ] Create migration files:
  - `V001__initial_schema.sql` — tenants, workflows, workflow_steps, incidents, audit_logs
  - `V002__add_vector_extension.sql` — pgvector extension, embeddings table
  - `V003__add_events_table.sql` — raw events, event processing log
- [ ] Create JPA entities in `common/`:
  ```java
  @Entity @Table(name = "workflows")
  public class Workflow { ... }
  
  @Entity @Table(name = "incidents")
  public class Incident { ... }
  
  @Entity @Table(name = "audit_logs")
  public class AuditLog { ... }
  ```
- [ ] Create Spring Data JPA repositories
- [ ] Write seed data script

**Acceptance Criteria:**
- `mvn flyway:migrate` runs all migrations
- JPA entities map correctly to all tables
- Seed data populates test records

### Week 3: Kafka Setup & Ingestion Service [Spring Boot]

**Tasks:**
- [ ] Configure Spring Kafka in `common/`:
  - Producer with `acks=all`, idempotency enabled
  - JSON serializer/deserializer for events
  - Consumer group configuration
- [ ] Build `ingestion-service`:
  - `WebhookController` — receive GitHub, PagerDuty, AlertManager webhooks
  - `GitHubEventParser`, `PagerDutyEventParser` — parse payloads
  - `EventDeduplicationService` — Redis-based dedup
  - `EventPublisher` — publish to Kafka `aegisops.events.raw`
- [ ] Write webhook signature verification (HMAC-SHA256)
- [ ] Write integration tests with embedded Kafka (`spring-kafka-test`)

**Acceptance Criteria:**
- Can `curl` a GitHub webhook → event appears in Kafka topic
- Duplicate events are rejected (Redis dedup)
- Webhook signatures are validated

### Week 4: API Gateway & Auth Service [Spring Boot]

**Tasks:**
- [ ] Build `api-gateway` with Spring Cloud Gateway:
  - Route definitions for all services
  - JWT validation filter
  - Rate limiting (Redis-backed)
  - Correlation ID injection
- [ ] Build `auth-service`:
  - OAuth2 login with Google + GitHub (Spring Security)
  - JWT issuance (access token + refresh token)
  - RBAC role checking
  - API key management for webhook integrations
- [ ] Create REST endpoints for workflow CRUD:
  ```java
  @RestController
  @RequestMapping("/api/v1/workflows")
  public class WorkflowController {
      @GetMapping
      public Page<WorkflowDTO> listWorkflows(Pageable pageable) { ... }
      
      @PostMapping
      @PreAuthorize("hasAnyRole('ADMIN','SRE','PLATFORM_ENGINEER','DEVELOPER')")
      public WorkflowDTO createWorkflow(@RequestBody CreateWorkflowRequest request) { ... }
  }
  ```
- [ ] Write controller tests with `@WebMvcTest`

**Acceptance Criteria:**
- API Gateway routes requests to correct services
- JWT authentication works (reject invalid tokens)
- RBAC blocks unauthorized access
- OpenAPI docs served at `/swagger-ui.html`

### Week 5: Protobuf Contracts & Cross-Language Setup

**Tasks:**
- [ ] Define `.proto` files for all inter-service contracts:
  - `events.proto` — event schemas
  - `retrieval.proto` — search request/response
  - `agent.proto` — agent task/result
  - `memory.proto` — memory store/recall
  - `common.proto` — shared enums and types
- [ ] Set up Java protobuf code generation (`protobuf-maven-plugin`)
- [ ] Set up Python protobuf code generation (`grpcio-tools`)
- [ ] Create Python service skeleton (`agent-runtime/`, `retrieval-service/`, `memory-service/`)
- [ ] Verify Java → Python gRPC call works (simple echo test)
- [ ] Verify Java → Kafka → Python consumer works

**Acceptance Criteria:**
- Protobuf generates Java classes and Python stubs from same `.proto` files
- Java Spring Boot can call Python gRPC server
- Java produces Kafka message → Python consumes it

### Week 6: End-to-End Integration & Phase 1 Verification

**Tasks:**
- [ ] End-to-end: webhook → Kafka → database → API query
- [ ] Set up GitHub Actions CI (Java build + Python lint + proto check)
- [ ] Write integration tests across Java and Python
- [ ] Document Phase 1 in `docs/architecture.md`

**Phase 1 Verification Checklist:**
- [ ] Maven multi-module project builds cleanly
- [ ] Docker Compose brings up all infrastructure
- [ ] Flyway migrations apply successfully
- [ ] Ingestion service receives webhooks and publishes to Kafka
- [ ] API Gateway routes and authenticates requests
- [ ] Spring Security RBAC works
- [ ] Protobuf contracts generate code for both Java and Python
- [ ] gRPC communication works between Java and Python
- [ ] CI pipeline passes (Java + Python)

---

## Phase 2 — Core AI Agent Pipeline [Python]

### Goal
Build the multi-agent AI runtime in Python with LangGraph, implement the RAG pipeline, and connect it to the Spring Boot backbone via Kafka + gRPC.

### Timeline: 4-5 weeks

### Week 7: RAG Pipeline — Embedding & Indexing (Python)

**Tasks:**
- [ ] Implement embedding service with caching (Redis)
- [ ] Implement document chunkers: code (AST-aware), log (time-window), doc (paragraph)
- [ ] Create GitHub repo ingestion pipeline (clone → chunk → embed → pgvector)
- [ ] Store embeddings in pgvector via asyncpg

### Week 8: RAG Pipeline — Hybrid Search & Reranking (Python)

**Tasks:**
- [ ] Implement hybrid search (pgvector + PostgreSQL FTS + RRF merge)
- [ ] Implement cross-encoder reranking
- [ ] Build gRPC server for Retrieval Service (called by Java)
- [ ] Write retrieval quality tests

### Week 9: Agent Runtime — LangGraph + Kafka Integration (Python)

**Tasks:**
- [ ] Build LangGraph agent graph (supervisor, planner, retrieval, executor, critic, verification)
- [ ] Implement Kafka consumer: receive tasks from Spring Boot Orchestrator
- [ ] Implement Kafka producer: send results back to Spring Boot
- [ ] Create agent prompt templates in `prompts/`

### Week 10: Tool Integration & Agent Testing (Python)

**Tasks:**
- [ ] Implement agent tools (kubectl, github, metrics)
- [ ] Add OpenTelemetry tracing to Python services
- [ ] Write comprehensive agent tests (mocked LLM responses)
- [ ] End-to-end: Kafka task → LangGraph investigation → Kafka result

**Phase 2 Verification Checklist:**
- [ ] RAG pipeline indexes and retrieves relevant documents
- [ ] Hybrid search outperforms vector-only
- [ ] All 6 agents function correctly in LangGraph
- [ ] Python services consume/produce Kafka messages
- [ ] Java → gRPC → Python retrieval works
- [ ] End-to-end: webhook → Java → Kafka → Python agents → Kafka → Java result

---

## Phase 3 — Workflow Orchestration & Dashboard [Java + React]

### Goal
Integrate Temporal (Java SDK) for durable workflows, build the real-time dashboard with WebSocket streaming, and create the memory service.

### Timeline: 5-6 weeks

### Week 11-12: Temporal Workflow Integration (Spring Boot)

**Tasks:**
- [ ] Set up Temporal Java SDK in `workflow-orchestrator`
- [ ] Implement `IncidentInvestigationWorkflow` (plan → retrieve → analyze → verify → approve)
- [ ] Implement `BuildAnalysisWorkflow` and `DeploymentVerificationWorkflow`
- [ ] Implement activities that bridge to Python via Kafka
- [ ] Human-in-the-loop via Temporal signals

### Week 13: Memory Service (Python)

**Tasks:**
- [ ] Implement short-term memory (Redis), long-term memory (PostgreSQL), semantic memory (pgvector)
- [ ] Implement memory compression and `recall_similar`
- [ ] Connect to Agent Runtime

### Week 14-15: Real-Time Dashboard (Next.js + Spring WebSocket)

**Tasks:**
- [ ] Initialize Next.js frontend
- [ ] Build dashboard pages: home, workflow detail, incident list, analytics
- [ ] Implement STOMP WebSocket client for real-time updates
- [ ] Spring Boot `WorkflowStreamService` broadcasts Kafka events to WebSocket
- [ ] Style with dark theme, glassmorphism, smooth animations

### Week 16: Integration & Phase 3 Verification

- [ ] Connect dashboard to live API
- [ ] End-to-end: webhook → Temporal workflow → agents → dashboard streaming

---

## Phase 4 — Production Intelligence [3-4 weeks]

### Week 17: Kubernetes Integration (Python tools + Java event watcher)
### Week 18: Cross-System Incident Correlation (Java + Python)
### Week 19: Notification Service (Spring Boot — Slack, email, escalation)
### Week 20: Phase 4 Verification & Integration Testing

---

## Phase 5 — Autonomous Operations [3-4 weeks]

### Week 21: Remediation Proposal Engine (Python) + GitHub PR Creation
### Week 22: Canary Verification (Spring Boot) + Safety Scoring
### Week 23: Reflection Agent (Python) + Learning Loop

---

## Phase 6 — Enterprise & Hardening [3-4 weeks]

### Week 24: Multi-Tenancy (Spring Boot RLS + per-tenant isolation)
### Week 25: SSO (Spring Security SAML) + Audit + Compliance
### Week 26: Cost Analytics (Spring Boot) + Grafana Dashboards + Load Testing
### Week 27: Documentation + Final Security Review

---

# 31. Resume Impact

### Resume Line (After Completion)

> Architected and built **AEGISOPS**, an enterprise-grade AI-native engineering operations platform using a **polyglot microservices architecture (Spring Boot + Python)**. Spring Boot (Java 21) handles API gateway, OAuth2/RBAC (Spring Security), workflow orchestration (Temporal), and event ingestion (Spring Kafka). Python services power multi-agent investigation (LangGraph), hybrid RAG pipeline (pgvector + sentence-transformers), and autonomous verification. Cross-service communication via **Apache Kafka + gRPC (Protobuf)**. Reduced simulated MTTR by 60%+ through cross-system incident correlation. Deployed on Kubernetes with OpenTelemetry observability and multi-tenant isolation.

### What This Demonstrates

| Skill Area | Tech Used |
|---|---|
| **Backend Engineering** | Spring Boot, Spring Data JPA, Spring Cloud Gateway, REST, gRPC |
| **AI Systems Engineering** | LangGraph, RAG pipeline, multi-agent orchestration |
| **Distributed Systems** | Apache Kafka, Temporal workflows, saga pattern |
| **Polyglot Architecture** | Java ↔ Python via Protobuf contracts + Kafka |
| **Security** | Spring Security, OAuth2, RBAC, JWT, mTLS |
| **Kubernetes & DevOps** | Helm, HPA, Terraform, GitHub Actions CI/CD |
| **Observability** | Micrometer, OpenTelemetry, Prometheus, Grafana |

### Target Roles

| Role | Strongest Sections |
|---|---|
| **Backend Engineer (Java)** | Spring Boot microservices, Kafka, Temporal, Spring Security |
| **AI/ML Engineer** | Multi-agent architecture, RAG, evaluation system |
| **Platform Engineer** | Kubernetes, CI/CD, Terraform, observability |
| **Solutions Architect** | Polyglot architecture, system design, enterprise features |

---

# 32. Final Notes

### The Polyglot Advantage

Using Spring Boot + Python is not a compromise — it's the **optimal architecture** for this problem domain:

- Spring Boot excels at: typed APIs, enterprise security, Kafka consumers, workflow orchestration
- Python excels at: AI/ML agent frameworks, embedding pipelines, NLP tooling
- The boundary between them (Kafka + gRPC) is clean, well-defined, and industry-standard

### The Real Engineering Is Not the AI

The AI layer (agents, LLM calls, RAG) is ~20% of the system. The remaining 80% is:

| Component | Engineering Challenge |
|---|---|
| **Workflow Reliability** | Temporal, saga pattern, DLQs (Spring Boot) |
| **Distributed Orchestration** | Kafka topics, consumer groups, backpressure |
| **Cross-Language Integration** | Protobuf contracts, gRPC, Kafka serialization |
| **Security** | Spring Security, OAuth2, RBAC, audit logging |
| **Observability** | Micrometer (Java) + opentelemetry (Python) unified in Grafana |
| **Infrastructure** | Kubernetes, Helm, Terraform, autoscaling |

### Build Order Recommendations

1. **Phase 1 is critical** — Get the Java scaffolding, Kafka, and proto contracts right. Everything else depends on this.
2. **Phase 2 is the hardest** — The Python AI pipeline is where most unknowns live. Budget extra time.
3. **Phase 3 makes it demo-able** — Temporal + Dashboard is what impresses in interviews.
4. **Don't skip the proto contracts** — They're the glue between Java and Python. Getting these wrong creates debugging nightmares.

### Risk Mitigation

| Risk | Mitigation |
|---|---|
| Java ↔ Python integration issues | Define proto contracts first, test gRPC in Week 5 |
| LLM API costs | Model routing (Python), cache aggressively (Redis) |
| Complexity overwhelming | Follow phase plan strictly, simplest implementation first |
| No real K8s cluster | Minikube/Kind locally, mock in unit tests |
| Spring Boot cold start time | Use Spring Boot 3.x AOT, or GraalVM native image for lightweight services |

---

*This guide is a living document. Update it as you build each phase.*
