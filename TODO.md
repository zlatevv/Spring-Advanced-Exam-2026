Core required sections (not bonuses, but easy to half-finish and forget):

Caching: @Cacheable/@CacheEvict on manuscripts (planned, not yet built) + user profile/list (built, just added)
Scheduling: cron job (access request auto-expiry) — not started
Scheduling: non-cron trigger (digitization status polling) — not started
Feign client wired from Main app → digitization microservice — not started
Two @ControllerAdvice handlers (built-in exception + custom exception) — you have several custom exceptions now (UserAlreadyExistsException, EmailExistsException, LastAdminException, UserNotFoundException) but confirm the actual @ControllerAdvice class mapping them to responses exists
Logging on every functionality (AOP aspect) — not started
Tests: unit (service layer, both apps), @DataJpaTest, MockMvc/API test — not started
Security: confirm open/authenticated/authorized all represented (mostly done, just fixed the users gap)

Bonuses you specifically chose to pursue:

JWT — done
Redis caching — currently running on simple in-memory cache; swap to Redis config later (cheap, just a property change)
AOP Advice — tied to the logging requirement above, not started
Spring Events — not started (e.g. access-request-approved → notification listener)
Docker Compose for both apps — not started
Permissions in addition to roles — not started, optional

Deliberately parked / decided against for now:

Kafka — parked until required work is solid
Non-relational DB bonus — skipped per earlier reasoning (conflicts with JPA requirement on domain entities)
Refresh token rotation — decided against, out of scope