# CQRS-spring-boot
Implementation for CQRS Read DB and Write DB 
## The project consist 
 - CQRS lib to handle CQRS abstractions
 - Command microservice
 - Query microservice
 
## Technologies
  - Mongodb for event store
  - Postgres Read Database
  - Kafka event bus with at least once idempotency Manual commit only if publish successfully (Duplicates can happened)
  - Spring-boot framework 
  - JAPA

## Patterns
 - CQRS
 - DDD
 - event sourcing
 - Optimistic concurrency control
 
