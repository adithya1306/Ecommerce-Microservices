🛒 E-Commerce Microservices Backend

This is a production-grade e-commerce backend system built using Spring Boot microservices architecture, designed for scalability, modularity, and clarity. It covers essential backend features such as user authentication, product management, inventory tracking, order processing, and inter-service communication.


🚀 Features

- ✅ Spring Boot Microservices Architecture
- 🔑 JWT Authentication via API Gateway
- 🛂 Role-Based Access Control (RBAC) for admin/user differentiation
- 🧩 Eureka Service Discovery
- 🌐 Spring Cloud Gateway as API Gateway with centralized authentication
- 🔄 Inter-service communication using Spring WebClient
- 📦 Inventory auto-update on new orders
- ✉️ Order confirmation emails via Jakarta Mail
- 🔐 Custom JWT Filter with user and role propagation
- 🧪 REST endpoints secured using custom headers (`X-Username`, `X-Role`)
- 📄 Well-structured service-wise REST APIs


🧱 Microservices Structure

ecommerce-backend/ 
- api-gateway/ # Entry point with JWT filter and routing
- eureka-server/ # Eureka server for service registration
- product-service/ # Handles product listings and visibility
- user-service/ # User registration, login, and token generation
- order-service/ # Places orders, sends confirmation emails
- inventory-service/ # Manages product quantity and availability


🛠️ Tech Stack

- Java 17
- Spring Boot 3.2+
- Spring Cloud Gateway
- Spring Cloud Eureka Discovery
- Spring WebClient
- Spring Data JPA + any SQL DB (I used Postgres)
- Jakarta Mail (JavaMail)
- JWT (jjwt)
- Maven

🔐 Authentication Flow

1. Login via `/users/login` generates a JWT.
2. The Gateway intercepts requests and validates the token.
3. User details & roles are injected into downstream services via custom headers:
   - `X-Username`
   - `X-Role`
4. Role-based endpoints (e.g., `/inventory/all`) are protected using this info.



📬 Email Integration

- Order confirmations are emailed to users using Jakarta Mail over Gmail SMTP.
- Triggered automatically on successful order placement.


🧪 Testing Instructions

- Import into IntelliJ or your preferred IDE.
- Run the Eureka server (`discovery-server`) first.
- Start all services individually.
- Use Postman to:
  - Register/Login and get a JWT.
  - Add `Authorization: Bearer <your_token>` in headers.
  - Access protected endpoints across services.

📦 Sample Endpoints

| Service           | Endpoint                        | Method | Role Required  |
|-------------------|---------------------------------|--------|----------------|
| User Service      | `/users/register`               | POST   | Public         |
| User Service      | `/users/login`                  | POST   | Public         |
| Product Service   | `/products/add`                 | POST   | Admin only     |
| Product Service   | `/products/all`                 | GET    | Public         |
| Order Service     | `/orders/user`                  | GET    | User           |
| Inventory Service | `/inventory/all`                | GET    | Admin only     |

