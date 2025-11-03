# 🪙 Crypto Trading System (Spring Boot + H2)

This project is a **Spring Boot backend** for a simple crypto trading simulation.  
It features **price aggregation**, **wallet management**, and **trade simulation** using an **in-memory H2 database**.

---

## 🚀 Getting Started

### 1️⃣ Run the Project

You can start the Spring Boot application using Maven:

```bash
./mvnw spring-boot:run
```

or, if you have Maven installed globally:

```bash
mvn spring-boot:run
```

---

### 2️⃣ Access the H2 Database Console

Once the project starts successfully, open your browser and go to:

👉 [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

Use the following credentials:

| Property     | Value                |
|--------------|----------------------|
| **JDBC URL** | `jdbc:h2:mem:testdb` |
| **Username** | `sa`                 |
| **Password** | *(leave blank)*      |

Click **Connect** to view tables and data.

---

### 3️⃣ Test the APIs with Postman

Use the provided **Postman collection** to test the available API endpoints.

1. Import the file: [CryptoTrading.postman_collection.json](./CryptoTrading.postman_collection.json) into Postman.
2. Run the API calls in order — for example:
    - Fetch latest aggregated prices
    - Perform trade (buy/sell)
    - Retrieve wallet balance

---

## ⚙️ Configuration

Application settings can be modified in:

```
src/main/resources/application.properties
```

Example:

```properties
server.port=8080
scheduler.price-poll-interval-ms=10000
crypto.tickers.supported=BTCUSDT,ETHUSDT
spring.h2.console.enabled=true
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=create-drop
```

---

## 🧪 Running Tests

To execute unit and integration tests:

```bash
mvn test
```

---

## 🧩 Key Features

- 🔄 **Price Aggregation** from Binance and Huobi every 10 seconds
- 💰 **Wallet Management** for simulated crypto balances
- 💱 **Buy/Sell Trade Simulation** using aggregated prices
- 🗄️ **H2 In-Memory Database** for fast development and testing
- 🧪 **JUnit 5 + Mockito** for testing

---
