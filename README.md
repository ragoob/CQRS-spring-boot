# CQRS-spring-boot
Implementation for CQRS Read DB and Write DB 
## The project consist 
 - CQRS lib to handle CQRS abstractions
 - Command microservice
 - Query microservice
 
## Technologies
  - Mongodb for event store
  - Postgres Read Database
  - Kafka event bus with at least once idempotency Manual commit only if message proccessed successfully to read database (Duplicates can happen)
  - Spring-boot framework 
  - JPA

## Patterns
 - CQRS
 - Mediator
 - DDD
 - event sourcing
 - Optimistic concurrency control
 
## Notes
I can see framework code mixed with business code, I think better to have clear separation.

I can see you did a great job by have separate library `cqrs.core`, so I can see we might go further in this direction. 
(see commands in AccountCommandHandler.java for example)
