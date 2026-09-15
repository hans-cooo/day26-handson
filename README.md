# E-Commerce Application Performance Optimization

A simple e-commerce application built with **Java 21, Spring Boot 4.0.8, Spring Data JPA, H2, and Apache Tomcat 11**.

The application is designed as the baseline system for a performance optimization assignment involving:

1. JVM profiling
2. Load testing
3. Redis caching
4. Performance comparison and reporting

The application is packaged as a **WAR file** and deployed to a local Apache Tomcat 11 server running in Docker.

---

## Technology Stack

* **Java:** 21
* **Spring Boot:** 4.0.8
* **Spring Data JPA**
* **Spring Web MVC**
* **H2 Database**
* **Apache Tomcat:** 11
* **Maven**
* **Docker / Docker Compose**

---

## Project Structure

```text
ecommerceapp/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/ecommerceapp/
│   │   │       ├── config/
│   │   │       │   └── DataInitializer.java
│   │   │       │
│   │   │       ├── controller/
│   │   │       │   ├── OrderController.java
│   │   │       │   └── ProductController.java
│   │   │       │
│   │   │       ├── model/
│   │   │       │   ├── Order.java
│   │   │       │   └── Product.java
│   │   │       │
│   │   │       ├── repository/
│   │   │       │   ├── OrderRepository.java
│   │   │       │   └── ProductRepository.java
│   │   │       │
│   │   │       ├── service/
│   │   │       │   ├── OrderService.java
│   │   │       │   └── ProductService.java
│   │   │       │
│   │   │       ├── EcommerceappApplication.java
│   │   │       └── ServletInitializer.java
│   │   │
│   │   └── resources/
│   │
├── Dockerfile
├── docker-compose.yml
├── pom.xml
├── .gitignore
└── README.md
```

---

# Application Features

## Product API

The product service provides endpoints for retrieving and creating products.

### Get all products

```http
GET /ecommerceapp/api/products
```

Example:

```text
http://localhost:8080/ecommerceapp/api/products
```

The application starts with five sample products:

| ID | Product        |    Price | Stock |
| -: | -------------- | -------: | ----: |
|  1 | Laptop         | 75000.00 |    10 |
|  2 | Wireless Mouse |  1500.00 |    25 |
|  3 | Keyboard       |  2500.00 |    20 |
|  4 | Monitor        | 12000.00 |    15 |
|  5 | USB-C Cable    |   800.00 |    50 |

### Get a product by ID

```http
GET /ecommerceapp/api/products/{id}
```

Example:

```text
http://localhost:8080/ecommerceapp/api/products/2
```

### Create a product

```http
POST /ecommerceapp/api/products
```

Example JSON:

```json
{
  "name": "Webcam",
  "price": 3500,
  "stock": 15
}
```

---

# Order API

The order service supports retrieving and creating orders.

## Get all orders

```http
GET /ecommerceapp/api/orders
```

Example:

```text
http://localhost:8080/ecommerceapp/api/orders
```

## Get an order by ID

```http
GET /ecommerceapp/api/orders/{id}
```

Example:

```text
http://localhost:8080/ecommerceapp/api/orders/1
```

## Create an order

```http
POST /ecommerceapp/api/orders
```

Request body:

```json
{
  "productId": 2,
  "quantity": 3,
  "customer": "Customer 1"
}
```

The client does **not** need to provide `totalPrice`.

The application looks up the product price and calculates the total automatically.

For example:

```text
Wireless Mouse = 1500
Quantity       = 3

Total          = 1500 × 3
               = 4500
```

Example response:

```json
{
  "productId": 2,
  "quantity": 3,
  "customer": "Customer 1",
  "totalPrice": 4500.0,
  "id": 1
}
```

---

# Running the Application

## Prerequisites

Install the following:

* Java 21
* Maven
* Docker Desktop
* Git

Verify the installations:

```powershell
java -version
mvn -version
docker --version
docker compose version
```

---

# Option 1: Run Using Docker

Docker is the recommended method because the assignment requires the application to run on a local Tomcat server.

## 1. Clone the repository

```powershell
git clone <REPOSITORY-URL>
cd ecommerceapp
```

Replace `<REPOSITORY-URL>` with the URL of this repository.

## 2. Build the WAR

```powershell
mvn clean package
```

The build should finish with:

```text
BUILD SUCCESS
```

This creates:

```text
target/ecommerceapp-0.0.1-SNAPSHOT.war
```

## 3. Start Tomcat

```powershell
docker compose up --build
```

Docker will:

1. Build the application image.
2. Start Apache Tomcat 11.
3. Deploy the Spring Boot WAR.
4. Expose Tomcat on port 8080.

The application will be available at:

```text
http://localhost:8080/ecommerceapp
```

---

# Testing the Application

Open another PowerShell terminal while Docker Compose is running.

## Test Products

```powershell
curl.exe http://localhost:8080/ecommerceapp/api/products
```

Expected response:

```json
[
  {
    "name": "Laptop",
    "price": 75000.0,
    "stock": 10,
    "id": 1
  }
]
```

The complete response contains five products.

---

## Test Product by ID

```powershell
curl.exe http://localhost:8080/ecommerceapp/api/products/2
```

Expected result:

```json
{
  "name": "Wireless Mouse",
  "price": 1500.0,
  "stock": 25,
  "id": 2
}
```

---

## Test Orders

Get all orders:

```powershell
curl.exe http://localhost:8080/ecommerceapp/api/orders
```

A fresh application normally returns:

```json
[]
```

---

## Create an Order Using PowerShell

PowerShell's `Invoke-RestMethod` is recommended for POST requests because it handles JSON more reliably than the Windows `curl` alias.

Create the request body:

```powershell
$body = @{
    productId = 2
    quantity = 3
    customer = "Customer 1"
} | ConvertTo-Json
```

Send the request:

```powershell
Invoke-RestMethod `
    -Uri "http://localhost:8080/ecommerceapp/api/orders" `
    -Method Post `
    -ContentType "application/json" `
    -Body $body
```

Expected result:

```text
productId  : 2
quantity   : 3
customer   : Customer 1
totalPrice : 4500.0
id         : 1
```

Verify the order:

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/ecommerceapp/api/orders"
```

---

# Important: H2 Database

The application currently uses an **in-memory H2 database**.

This means the sample products and orders are reset when the application restarts.

The `DataInitializer` class automatically creates the five sample products whenever the application starts.

This is intentional for the baseline performance-testing application.

Do not assume that data created during one test run will still exist after restarting Docker.

---

# Docker Commands

### Start the application

```powershell
docker compose up --build
```

### Stop the application

Press:

```text
Ctrl + C
```

### Stop and remove the container

```powershell
docker compose down
```

### Rebuild after code changes

```powershell
mvn clean package
docker compose up --build
```

### Check running containers

```powershell
docker ps
```

### View application logs

```powershell
docker compose logs -f
```

---

# Performance Testing Baseline

This repository represents the **baseline version** of the application before performance optimizations.

Please preserve this behavior when beginning the performance tasks.

The main endpoints that can be used for testing are:

```text
GET  /ecommerceapp/api/products
GET  /ecommerceapp/api/products/{id}
GET  /ecommerceapp/api/orders
GET  /ecommerceapp/api/orders/{id}
POST /ecommerceapp/api/orders
```

These endpoints can be used for:

* JVM profiling
* CPU profiling
* memory analysis
* load testing
* response-time measurements
* throughput measurements
* Redis caching comparisons

---

# Performance Optimization Tasks

The project is intended to be extended through separate Git branches for the different performance tasks.

Recommended branches:

```text
main
feature/jvm-profiling
feature/load-testing
feature/redis-caching
```

The `main` branch should remain the clean baseline application.

## JVM Profiling

Use a JVM profiling tool such as VisualVM or JProfiler to identify:

* CPU-intensive operations
* memory usage
* garbage collection behavior
* thread activity
* potential bottlenecks

Do not modify the baseline application unnecessarily while collecting profiling data.

---

## Load Testing

Use a load-testing tool such as Apache JMeter or Gatling.

Possible test endpoints include:

```text
GET http://localhost:8080/ecommerceapp/api/products
```

and:

```text
GET http://localhost:8080/ecommerceapp/api/products/2
```

For order creation, use:

```text
POST http://localhost:8080/ecommerceapp/api/orders
```

with:

```json
{
  "productId": 2,
  "quantity": 3,
  "customer": "Load Test Customer"
}
```

Record metrics such as:

* Average response time
* Minimum response time
* Maximum response time
* Throughput
* Error rate
* Number of concurrent users

---

## Redis Caching

Redis will be introduced as a performance optimization.

The caching implementation should be compared against the current baseline.

The comparison should include:

* Response time without caching
* Response time with caching
* Throughput without caching
* Throughput with caching
* Cache hit behavior
* Cache miss behavior

The baseline version in `main` should remain available so that performance results can be compared fairly.

---

# Suggested Workflow for Group Members

Before beginning your assigned performance task:

### 1. Clone the repository

```powershell
git clone https://github.com/hans-cooo/day26-handson.git
cd ecommerceapp
```

### 2. Confirm the baseline works

```powershell
mvn clean package
docker compose up --build
```

### 3. Test the APIs

```powershell
curl.exe http://localhost:8080/ecommerceapp/api/products
```

and:

```powershell
curl.exe http://localhost:8080/ecommerceapp/api/orders
```

### 4. Create your own branch

For example:

```powershell
git checkout -b feature/jvm-profiling
```

or:

```powershell
git checkout -b feature/load-testing
```

or:

```powershell
git checkout -b feature/redis-caching
```

### 5. Perform your assigned work

Keep your changes limited to the requirements of your task.

### 6. Commit your work

```powershell
git add .
git commit -m "Add JVM profiling analysis"
```

Use an appropriate commit message for your task.

### 7. Push your branch

```powershell
git push -u origin feature/jvm-profiling
```

Replace the branch name with your assigned branch.

---

# Assignment Goal

The goal of this project is to analyze and improve the performance of a Java Spring Boot e-commerce application.

The performance workflow is:

```text
Baseline Application
        |
        +-------------------+
        |                   |
        v                   v
 JVM Profiling        Load Testing
        |                   |
        +---------+---------+
                  |
                  v
           Performance Data
                  |
                  v
            Redis Caching
                  |
                  v
        Performance Comparison
                  |
                  v
             Final Report
```

The final report should document the baseline system, profiling results, load-testing results, Redis caching implementation, performance comparisons, screenshots, conclusions, and recommendations.

---

# Notes

* The application is deployed as a WAR file to Apache Tomcat 11.
* Docker is used to provide the local Tomcat environment.
* The application uses an in-memory H2 database.
* Restarting the application resets the database.
* `totalPrice` is calculated by the server and should not be supplied by clients.
* Keep the `main` branch as the baseline whenever possible.
* Performance branches should be based on the baseline version.
