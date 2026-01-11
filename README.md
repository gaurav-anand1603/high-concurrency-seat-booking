# High-Concurrency Seat Reservation System

A backend system designed to handle concurrent seat bookings safely under high load.
The primary goal is to prevent double booking using distributed locking, caching,
and event-driven microservices.

### Known Issue (Concurrency)
- Concurrent booking requests can result in double booking
- Serializable isolation can prevent anomalies but introduces heavy database locking, poor scalability, and complex retry handling. For high-concurrency systems, distributed locking at the application level—such as Redis locks—is a more scalable and controlled solution.
- Added Redis-based distributed locking, eliminating race conditions and guaranteeing exactly-once booking under concurrent load