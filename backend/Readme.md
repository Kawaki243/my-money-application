# 💰 My Money Application - Backend

## 📌 Overview
The **My Money Application Backend** is a secure and scalable financial management service built with **Spring Boot**.  
It provides REST APIs for authentication, income & expense tracking, and reporting.  
JWT-based authentication ensures secure access for frontend and mobile clients.  

---

## 🚀 Features
- 🔐 **User Authentication** with JWT  
- 📊 Manage **incomes, expenses, and categories**  
- 📈 Generate **financial summaries & reports**  
- 🌍 REST API for integration with frontend & mobile apps  
- 🛡 Built with **Spring Security** for robust protection  
- 🗄 Database persistence with **JPA/Hibernate**  

---

## 🛠 Tech Stack
- **Framework**: Spring Boot  
- **Language**: Java 17+  
- **Security**: Spring Security + JWT  
- **Database**: MySQL 
- **ORM**: Hibernate / Spring Data JPA  
- **Build Tool**: Maven / Gradle  

---

## 📂 Project Structure
```
backend/
│── src/main/java/com/mymoneyapp/   # Application source code
│   ├── controller/                 # REST Controllers
│   ├── service/                    # Business Logic
│   ├── model/                      # Entities
│   ├── repository/                 # Data Access Layer
│   ├── security/                   # JWT + Security Config
│   └── MyMoneyApplication.java     # Main Entry Point
│
│── src/main/resources/
│   ├── application.properties      # Configuration
│   └── schema.sql / data.sql       # DB scripts (optional)
│
│── pom.xml / build.gradle          # Dependencies
│── README.md
```

---

## ⚙️ Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/Kawaki243/my-money-application.git
   cd my-money-application/backend
   ```

2. Configure database in `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/moneymanager
   spring.datasource.username=root
   spring.datasource.password=Dangcapmaimai123

   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true

   jwt.secret=your_secret_key
   jwt.expiration=86400000
   ```

3. Build the project:
   ```bash
   ./mvnw clean install
   ```

4. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

The server will start at 👉 `http://localhost:8080`

---

## 📡 API Endpoints

### 🔑 Authentication
| Method | Endpoint        | Description       | Auth Required |
|--------|----------------|------------------|---------------|
| POST   | `/api/auth/register` | Register a new user | ❌ |
| POST   | `/api/auth/login`    | Login and get JWT   | ❌ |

### 👤 Users
| Method | Endpoint       | Description              | Auth Required |
|--------|---------------|--------------------------|---------------|
| GET    | `/api/users/me` | Get current user profile | ✅ |

### 💸 Transactions
| Method | Endpoint                 | Description              | Auth Required |
|--------|--------------------------|--------------------------|---------------|
| GET    | `/api/transactions`      | Get all transactions     | ✅ |
| POST   | `/api/transactions`      | Create a transaction     | ✅ |
| PUT    | `/api/transactions/{id}` | Update a transaction     | ✅ |
| DELETE | `/api/transactions/{id}` | Delete a transaction     | ✅ |

---

## 🧪 Testing
Run unit and integration tests:
```bash
./mvnw test
```

---

## 🐳 Docker
To build and run with Docker:
```bash
docker build -t my-money-backend .
docker run -p 8080:8080 my-money-backend
```

---

## 🚀 Deployment
- Package the app:
  ```bash
  ./mvnw package
  ```
- Deploy `target/my-money-application.jar` to your server or cloud platform.  
- Configure PostgreSQL and environment variables accordingly.  

---

## 🤝 Contributing
1. Fork the repo  
2. Create a branch: `git checkout -b feature/my-feature`  
3. Commit: `git commit -m "Add feature"`  
4. Push: `git push origin feature/my-feature`  
5. Open a Pull Request  

---

## 📜 License
This project is licensed under the **MIT License**.  
