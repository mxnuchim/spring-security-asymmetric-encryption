# 🔐 Spring Security Asymmetric Encryption - JWT Authentication

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17.5-blue.svg)](https://www.postgresql.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](CONTRIBUTING.md)

> **Enterprise-grade Spring Boot Authentication System implementing JWT authentication with RSA asymmetric encryption.** I've used **private/public key pairs** for signing and verifying tokens to ensure secure, tamper-proof authentication across distributed services, with fine-grained access control and refresh token support for seamless user sessions.

---

## Asymmetric encryption

Most JWT implementations use **symmetric encryption** (HMAC) - a single secret shared across services. This project implements **asymmetric encryption** using RSA-2048 key pairs, providing:

- ✅ **Enhanced Security** - Private keys never leave the authentication service
- ✅ **Microservices Ready** - Share public keys safely across services
- ✅ **Industry Standard** - Used by OAuth 2.0, OpenID Connect, and enterprise systems
- ✅ **Production Ready** - Complete with validation, error handling, and API documentation

---

## 🚀 What's Inside

### Core Features

- 🔑 **RSA-2048 Asymmetric Encryption** for JWT signing and verification
- 🛡️ **Spring Security 6** with stateless session management
- 🔐 **Complete Authentication Flow** - Register, Login, Refresh Token
- ✅ **Custom Validation Annotations** - Including disposable email detection
- 🎯 **Global Exception Handling** - Standardized error responses
- 📊 **JPA Auditing** - Track who created/modified entities
- 📖 **OpenAPI Documentation** - Interactive Swagger UI
- 🐳 **Docker Compose** - PostgreSQL setup included
- 🏗️ **Clean Architecture** - Service layer, DTOs, and proper separation of concerns

### Security Features

- **JWT Access Tokens** (15 min recommended for production)
- **JWT Refresh Tokens** (7 days)
- **Password Encryption** with BCrypt
- **Role-Based Access Control** (RBAC)
- **Account Status Management** (enabled, locked, credentials expired)
- **Email & Phone Verification Flags**
- **Custom JWT Filter** for request authentication

---

## 🏗️ Architecture Overview
```
┌─────────────────┐
│   Client App    │
└────────┬────────┘
         │ JWT (Bearer Token)
         ▼
┌─────────────────┐
│  JWT Filter     │──┐
└────────┬────────┘  │
         │           │ Validates with
         ▼           │ Public Key
┌─────────────────┐  │
│   Controller    │◀─┘
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│    Service      │
└────────┬────────┘
         │
         ▼
┌─────────────────┐      ┌──────────────┐
│   Repository    │─────▶│  PostgreSQL  │
└─────────────────┘      └──────────────┘

JWT Signing:
┌─────────────────┐
│  Private Key    │──▶ Signs JWT
└─────────────────┘

JWT Verification:
┌─────────────────┐
│  Public Key     │──▶ Verifies JWT
└─────────────────┘
```

---

## 🛠️ Tech Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Spring Boot | 3.5.3 | Application Framework |
| Spring Security | 6.x | Security & Authentication |
| Spring Data JPA | 3.x | Data Persistence |
| PostgreSQL | 17.5 | Database |
| JJWT | 0.12.6 | JWT Library |
| Lombok | Latest | Reduce Boilerplate |
| SpringDoc OpenAPI | 2.7.0 | API Documentation |
| Docker | Latest | Containerization |
| Maven | 3.x | Build Tool |

---

## 📋 Prerequisites

- **Java 21** or higher
- **Maven 3.8+**
- **Docker & Docker Compose** (for PostgreSQL)
- **IntelliJ IDEA** (recommended) or any Java IDE

---

## 🚀 Quick Start

### 1. Clone the Repository
```bash
git clone https://github.com/mxnuchim/spring-security-asymmetric-encryption.git
cd spring-security-asymmetric-encryption
```

### 2. Generate RSA Key Pair
```bash
cd src/main/resources/keys/local-only

# Generate Private Key (RSA-2048)
openssl genpkey -algorithm RSA -out private_key.pem -pkeyopt rsa_keygen_bits:2048

# Extract Public Key
openssl rsa -pubout -in private_key.pem -out public_key.pem
```

### 3. Set Up Environment Variables

Create a `.env` file in the root directory:
```env
DB_URL=localhost
DB_PORT=5432
DB_NAME=spring_app_db
DB_USERNAME=username
DB_PASSWORD=password
```

### 4. Start PostgreSQL
```bash
docker-compose up -d
```

### 5. Run the Application
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

---

## 📚 API Documentation

Once the application is running, access the interactive API documentation:

**Swagger UI:** `http://localhost:8080/swagger-ui.html`

### Key Endpoints

#### Authentication

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/auth/register` | Register new user |
| POST | `/api/v1/auth/login` | Login and get tokens |
| POST | `/api/v1/auth/refresh` | Refresh access token |

#### User Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| PATCH | `/api/v1/users/me` | Update profile |
| POST | `/api/v1/users/me/password` | Change password |
| PATCH | `/api/v1/users/me/deactivate` | Deactivate account |
| PATCH | `/api/v1/users/me/reactivate` | Reactivate account |

---

## 🔑 How Asymmetric JWT Works

### Traditional Symmetric Approach (HMAC)
```
Secret Key ──▶ Sign JWT
Same Secret Key ──▶ Verify JWT
❌ Problem: Key must be shared across all services
```

### Our Asymmetric Approach (RSA)
```
Private Key ──▶ Sign JWT (Auth Service Only)
Public Key ──▶ Verify JWT (Any Service)
✅ Benefit: Private key stays secure, public key can be distributed
```

### Token Generation Flow
```java
// 1. Sign with Private Key (Authentication Service)
String jwt = Jwts.builder()
    .setClaims(claims)
    .setSubject(username)
    .setIssuedAt(new Date())
    .setExpiration(expiration)
    .signWith(privateKey)  // ← Private Key
    .compact();

// 2. Verify with Public Key (Any Service)
Claims claims = Jwts.parserBuilder()
    .setSigningKey(publicKey)  // ← Public Key
    .build()
    .parseClaimsJws(jwt)
    .getBody();
```

---

## 🔒 Security Features Deep Dive

### 1. Custom JWT Filter

- Intercepts every request
- Extracts JWT from Authorization header
- Validates token using public key
- Sets authentication in SecurityContext

### 2. Password Security

- BCrypt encryption (adaptive cost factor)
- Password validation (uppercase, lowercase, numbers, special chars)
- Confirm password matching

### 3. Custom Validations

#### Disposable Email Detection
```java
@NonDisposableEmail
private String email;
```
Blocks temporary/fake email services like:
- yopmail.com
- tempmail.com
- guerrillamail.com
- 20+ more domains

### 4. Account Management

- Account enabled/disabled
- Account locked
- Credentials expired
- Email verification status
- Phone verification status

---

## 📝 Configuration

### Application Properties
```yaml
spring:
  application:
    name: spring-security-asymmetric-encryption
  
  datasource:
    url: jdbc:postgresql://${DB_URL}:${DB_PORT}/${DB_NAME}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
    driver-class-name: org.postgresql.Driver
  
  jpa:
    hibernate:
      ddl-auto: update
    database: postgresql
    database-platform: org.hibernate.dialect.PostgreSQLDialect
    show-sql: false

app:
  security:
    jwt:
      access-token-expiration: 86400000  # 24 hours (use 900000 = 15 min for production)
      refresh-token-expiration: 604800000  # 7 days
    disposable-emails:
      - yopmail.com
      - tempmail.com
      - guerrillamail.com
      # ... more
```

---

## 🧪 Testing the Application

### 1. Register a User
```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Manuchim",
    "lastName": "Oliver",
    "email": "oliver@mail.com",
    "password": "Password123!",
    "confirmPassword": "Password123!",
    "phoneNumber": "1234567890"
  }'
```

### 2. Login
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "ali@mail.com",
    "password": "Password123!"
  }'
```

Response:
```json
{
  "accessToken": "eyJhbGciOiJSUzI1NiJ9...",
  "refreshToken": "eyJhbGciOiJSUzI1NiJ9...",
  "tokenType": "Bearer"
}
```

### 3. Access Protected Endpoint
```bash
curl -X PATCH http://localhost:8080/api/v1/users/me \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Updated Name"
  }'
```

### 4. Refresh Token
```bash
curl -X POST http://localhost:8080/api/v1/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{
    "refreshToken": "YOUR_REFRESH_TOKEN"
  }'
```
---

## 📖 Additional Resources

### Asymmetric Cryptography
- [RSA Algorithm Explained](https://en.wikipedia.org/wiki/RSA_(cryptosystem))
- [Public Key Infrastructure](https://en.wikipedia.org/wiki/Public_key_infrastructure)

### JWT Standards
- [RFC 7519 - JSON Web Token](https://datatracker.ietf.org/doc/html/rfc7519)
- [JWT.io Debugger](https://jwt.io/)

### Spring Security
- [Spring Security Reference](https://docs.spring.io/spring-security/reference/)
- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/)

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 🙏 Acknowledgments

- Spring Team for excellent documentation
- JWT.io for the token debugger
- Community feedback and contributions

---

## 📬 Contact

Manuchimso Oliver 
- Email: manuchimoliver779@gmail.com
- GitHub: [@mxnuchim](https://github.com/mxnuchim)
- LinkedIn: [Manuchim Oliver](https://linkedin.com/in/manuchimoliver)

---

<div align="center">

### ⭐ If you found this helpful, please star the repository! ⭐

**Made with ❤️ by M Oliver**

</div>
