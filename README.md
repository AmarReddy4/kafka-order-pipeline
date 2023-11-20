# Kafka Order Pipeline

An event-driven order processing pipeline built with Spring Boot and Apache Kafka. Orders are submitted via REST API, published to Kafka, consumed and validated, then status updates are pushed to a separate topic.

Wanted to build a simple but realistic Kafka producer/consumer setup with Spring Boot to understand the event-driven pattern better.

## Tech Stack

- **Java 17**
- **Spring Boot 3.1.5** — Web + Kafka integration
- **Apache Kafka** — Message broker
- **Docker Compose** — Local Kafka + Zookeeper setup
- **Maven** — Build tool

## Architecture

```
POST /api/orders
       |
       v
 [OrderProducer] --> [orders topic] --> [OrderConsumer]
                                              |
                                              v
                                    [order-status topic]
                                              |
                                              v
                                    [Status Notification]
```

## Getting Started

### 1. Start Kafka

```bash
docker-compose up -d
```

### 2. Run the application

```bash
./mvnw spring-boot:run
```

### 3. Submit an order

```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "John Doe",
    "product": "Laptop",
    "quantity": 1,
    "price": 999.99
  }'
```

### 4. Check order status

```bash
# Get specific order
curl http://localhost:8080/api/orders/{orderId}

# List all processed orders
curl http://localhost:8080/api/orders
```

## Kafka Topics

| Topic | Purpose |
|-------|---------|
| `orders` | New orders submitted for processing (3 partitions) |
| `order-status` | Status updates after processing (CONFIRMED/FAILED) |

## What I Learned

- Kafka producer/consumer patterns with Spring Boot
- JSON serialization/deserialization with Jackson + Kafka
- Topic partitioning for parallel processing
- Consumer groups for scaling and notification separation
