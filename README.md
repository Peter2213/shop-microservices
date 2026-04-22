# Shop Microservices

A microservices-based e-commerce application built with Spring Boot that demonstrates inter-service communication and discount management through vouchers.

## 📋 Project Overview

This repository contains two independent Spring Boot microservices that work together to create a complete e-commerce system:

- **Voucher Service** (`my-voucher-app`) - Manages discount vouchers
- **Product Service** (`products-rest-api`) - Manages products with integrated discount application

The services communicate via REST API calls using Spring's `RestTemplate`, allowing the Product Service to fetch and apply discounts from the Voucher Service.

---

## 🏗️ Architecture

### Microservices Diagram

```
┌─────────────────────┐
│  Product Service    │
│  (Port 5550)        │
│  - CRUD Operations  │
│  - REST API         │
│  - Calls Voucher    │
│    Service          │
└──────────┬──────────┘
           │ (HTTP)
           │ RestTemplate
           ↓
┌─────────────────────┐
│  Voucher Service    │
│  (Port 5555)        │
│  - Voucher Mgmt     │
│  - Discount API     │
└─────────────────────┘
```

### Services

#### 1. **Voucher Service** (`my-voucher-app`)
- **Port**: `5555`
- **Database**: MySQL - `projectdb`
- **Purpose**: Manage discount vouchers and provide discount information

**Endpoints**:
- `POST /voucherapi/vouchers` - Create a new voucher
  ```json
  {
    "code": "SUMMER2024",
    "discount": 15.00
  }
  ```
- `GET /voucherapi/vouchers/{code}` - Get voucher by code

#### 2. **Product Service** (`products-rest-api`)
- **Port**: `5550`
- **Database**: MySQL - `productsdb`
- **Purpose**: Manage products with discount integration

**Endpoints**:
- `POST /productapi/products/` - Create product (applies voucher discount if provided)
  ```json
  {
    "name": "Laptop",
    "description": "High-performance laptop",
    "price": 1000.00,
    "voucherCode": "SUMMER2024"
  }
  ```
- `GET /productapi/products/` - Get all products
- `GET /productapi/products/{id}` - Get product by ID
- `PUT /productapi/products/{id}` - Update entire product
- `PATCH /productapi/products/{id}` - Partial update (name, description, or price)
- `DELETE /productapi/products/{id}` - Delete product

---

## 💻 Technologies Used

### Core Framework
- **Spring Boot** 4.0.5
- **Java** 17+
- **Maven** (Build tool)

### Data Access
- **Spring Data JPA** - ORM for database operations
- **MySQL Driver** - Database connectivity
- **H2 Database** - In-memory database (console available)

### Web & Communication
- **Spring Web MVC** - REST API development
- **RestTemplate** - Inter-service HTTP communication

### Additional Tools
- **Spring Boot H2 Console** - Database browser (http://localhost:8080/h2-console)

---

## 🔧 Prerequisites

Before running the application, ensure you have:

- **Java 17** or higher installed
- **Maven** installed (or use included `mvnw` wrapper)
- **MySQL Server** running locally (port 3306)
- **Git** for version control

**Check versions:**
```powershell
java -version
mvn -version
```

---

## 📦 Database Setup

### Create Databases

Connect to MySQL and create the required databases:

```sql
CREATE DATABASE projectdb;
CREATE DATABASE productsdb;
```

### Database Schema

The applications use **Spring Data JPA with Hibernate** to auto-create tables based on entity annotations.

**Voucher Service Tables:**
- `voucher` - Stores voucher information (code, discount)

**Product Service Tables:**
- `product` - Stores product information (name, description, price, voucher_code)

---

## 🚀 Running the Application

### Option 1: Using Maven

**Step 1: Start Voucher Service (Port 5555)**
```powershell
cd my-voucher-app
.\mvnw spring-boot:run
```

**Step 2: Start Product Service in another terminal (Port 5550)**
```powershell
cd products-rest-api
.\mvnw spring-boot:run
```

### Option 2: Build and Run JAR

**Build Voucher Service:**
```powershell
cd my-voucher-app
.\mvnw clean package
java -jar target/my-voucher-app-0.0.1-SNAPSHOT.jar
```

**Build Product Service:**
```powershell
cd products-rest-api
.\mvnw clean package
java -jar target/products-rest-api-0.0.1-SNAPSHOT.jar
```

---

## 🔐 Security - Credentials Management

**⚠️ Important:** Never commit sensitive credentials to version control.

### Setup Instructions
Configure database credentials securely:

**Option 1: Environment Variables** (Recommended for production)
```powershell
$env:DB_USERNAME="your_username"
$env:DB_PASSWORD="your_password"
```

Then reference in `application.properties`:
```properties
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```

**Option 2: application-secret.properties** (For local development)
Create a local `application-secret.properties` file (do NOT commit to git):

Add to `.gitignore`:
```
application-secret.properties
```

---

## 📋 Application Properties

### Voucher Service (`my-voucher-app/application.properties`)
```properties
spring.application.name=my-voucher-app
server.port=5555

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/projectdb
spring.datasource.username=<your_username>
spring.datasource.password=<your_password>

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### Product Service (`products-rest-api/application.properties`)
```properties
spring.application.name=products-rest-api
server.port=5550

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/productsdb
spring.datasource.username=<your_username>
spring.datasource.password=<your_password>

# Voucher Service URL
voucherService.url=http://localhost:5555/voucherapi/vouchers/

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

## 🔄 Service Communication Flow

### Workflow: Creating a Product with Discount

1. **Client** sends POST request to Product Service:
   ```
   POST http://localhost:5550/productapi/products/
   Body: { name: "Laptop", price: 1000.00, voucherCode: "SUMMER2024" }
   ```

2. **Product Service** receives request and:
   - Extracts `voucherCode` from request
   - Calls **Voucher Service**: `GET http://localhost:5555/voucherapi/vouchers/SUMMER2024`

3. **Voucher Service** responds with:
   ```json
   {
     "id": 1,
     "code": "SUMMER2024",
     "discount": 15.00
   }
   ```

4. **Product Service** applies discount:
   - Original Price: 1000.00
   - Discount: 15.00
   - **Final Price: 985.00**

5. Product is saved with discounted price

---

## 📊 Example Usage

### Using cURL or Postman

**Create a Voucher:**
```bash
curl -X POST http://localhost:5555/voucherapi/vouchers \
  -H "Content-Type: application/json" \
  -d '{"code":"SUMMER2024","discount":15}'
```

**Get Voucher:**
```bash
curl http://localhost:5555/voucherapi/vouchers/SUMMER2024
```

**Create Product with Discount:**
```bash
curl -X POST http://localhost:5550/productapi/products/ \
  -H "Content-Type: application/json" \
  -d '{
    "name":"Laptop",
    "description":"Gaming Laptop",
    "price":1000.00,
    "voucherCode":"SUMMER2024"
  }'
```

**Get All Products:**
```bash
curl http://localhost:5550/productapi/products/
```

**Get Product by ID:**
```bash
curl http://localhost:5550/productapi/products/1
```

**Update Product (Full):**
```bash
curl -X PUT http://localhost:5550/productapi/products/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Updated Laptop","description":"Updated Description","price":900.00}'
```

**Partial Update (PATCH):**
```bash
curl -X PATCH http://localhost:5550/productapi/products/1 \
  -H "Content-Type: application/json" \
  -d '{"price":850.00}'
```

**Delete Product:**
```bash
curl -X DELETE http://localhost:5550/productapi/products/1
```

---

## 📁 Project Structure

```
shop-microservices/
├── my-voucher-app/                          # Voucher Service
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/my_voucher_app/
│   │   │   │   ├── MyVoucherAppApplication.java
│   │   │   │   ├── controllers/VoucherController.java
│   │   │   │   ├── model/Voucher.java
│   │   │   │   └── repo/VoucherRepo.java
│   │   │   └── resources/application.properties
│   │   └── test/
│   ├── pom.xml
│   └── mvnw
│
├── products-rest-api/                       # Product Service
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/products_rest_api/
│   │   │   │   ├── ProductsRestApiApplication.java
│   │   │   │   ├── controller/productsController.java
│   │   │   │   ├── dto/Voucher.java
│   │   │   │   ├── Entities/Product.java
│   │   │   │   └── repo/ProductRepository.java
│   │   │   └── resources/application.properties
│   │   └── test/
│   ├── pom.xml
│   └── mvnw
│
└── README.md                                 # This file
```

---

## 🐛 Troubleshooting

| Issue | Solution |
|-------|----------|
| **Connection refused** | Ensure MySQL is running on port 3306 |
| **Databases don't exist** | Create `projectdb` and `productsdb` manually in MySQL |
| **Port already in use** | Change port in `application.properties` (e.g., server.port=5551) |
| **RestTemplate error** | Ensure Voucher Service is running before Product Service |
| **401/403 errors** | Check CORS configuration if accessing from frontend |

---

## 🧪 Testing the Services

### 1. Test Voucher Service Independently
```powershell
# Terminal 1: Start Voucher Service
cd my-voucher-app
.\mvnw spring-boot:run

# Terminal 2: Test endpoints
curl http://localhost:5555/voucherapi/vouchers/SUMMER2024
```

### 2. Test Product Service with Service Call
```powershell
# Terminal 1: Start Voucher Service
cd my-voucher-app
.\mvnw spring-boot:run

# Terminal 2: Start Product Service
cd products-rest-api
.\mvnw spring-boot:run

# Terminal 3: Create voucher, then product
curl -X POST http://localhost:5555/voucherapi/vouchers -H "Content-Type: application/json" -d '{"code":"TEST","discount":10}'
curl -X POST http://localhost:5550/productapi/products/ -H "Content-Type: application/json" -d '{"name":"Test Product","description":"Test","price":100,"voucherCode":"TEST"}'
```

---

## 🚀 Deployment

For production deployment:

1. **Use environment variables** for sensitive data (database credentials, service URLs)
2. **Set proper database connection pooling** in `application.properties`
3. **Enable HTTPS** for inter-service communication
4. **Implement API Gateway** for routing requests
5. **Add logging and monitoring** (Spring Cloud, Actuator)
6. **Containerize with Docker** for consistent deployment

---

## 📝 License

This project is open source and available for educational purposes.

---

## 👨‍💻 Author

**Peter2213** - Microservices Development

---

## 📞 Support

For issues or questions, please create an issue in the repository.

---

**Last Updated:** April 22, 2026
