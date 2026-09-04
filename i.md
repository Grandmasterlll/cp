| Категория | Технологии |
|---|---|
| Язык / JVM | Java 17+, Stream API |
| Фреймворки | Spring Boot 3, Spring Security 6 (JWT, OAuth 2.1) |
| Обмен сообщениями | Apache Kafka, Kafka Streams, Confluent Schema Registry (Avro) |
| БД | PostgreSQL 15 (OLTP), MongoDB 7 (документы), Redis 7 (кэш, Redlock, rate-limiting) |
| RPC | gRPC (внутренние вызовы с низкой задержкой) |
| Инфраструктура | Docker (Buildx), Kubernetes (k3d, Helm-чарты, sealed-secrets) |
| CI/CD | GitHub Actions: checkstyle, unit-тесты, coverage ≥ 80 %, Testcontainers |
| Наблюдаемость | Prometheus + Grafana, Loki (JSON-логи), OpenTelemetry + Jaeger |
| Безопасность | OWASP Top-10, TLS 1.3, OWASP Dependency-Check, Trivy |