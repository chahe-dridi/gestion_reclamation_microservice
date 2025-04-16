
# 🛠️ **Reclamation Service**

This microservice handles user **reclamations (complaints/feedback)** in a distributed microservice-based application. It supports adding, retrieving, and processing complaints, along with generating QR codes and PDF reports.

---

## 📦 **Technologies Used**

- **Java 17** – The primary programming language.
- **Spring Boot 3** – Framework used for building the application.
- **Spring Data JPA** – For data persistence.
- **Spring Cloud** – Includes Eureka Client for service discovery and Spring Cloud Config for externalized configuration.
- **H2 Database** – In-memory database for development and testing.
- **Docker** – For containerizing the microservice.
- **QR Code Generation** – For generating unique QR codes for reclamations.

---

## 🚀 **How to Run**

### **Prerequisites**

Before running the service, make sure you have the following installed:

- **JDK 17+**
- **Maven** – For building the project.
- **Docker** (optional but recommended)
- **Eureka Server** – Running on `http://localhost:8761/` for service discovery.
- **Spring Cloud Config Server** – Running on `http://localhost:8888/` for externalized configuration.

### **Running with Docker**

1. **Build the JAR:**

   ```bash
   mvn clean package
   ```

2. **Build the Docker image:**

   ```bash
   docker build -t reclamation_service .
   ```

3. **Run the Docker container:**

   ```bash
   docker run -p 8084:8084 --name reclamation_service reclamation_service
   ```

Make sure port `8084` is available, or modify the port if necessary.

### **Accessing the Service**

Once the service is running, it will be available at:  
`http://localhost:8084/`

### **Eureka Server & Config Server**

- Ensure your **Eureka Server** is running at `http://localhost:8761/` to allow the service to register.
- The **Spring Cloud Config Server** should be running at `http://localhost:8888/` to provide external configuration.

---

## 🌐 **Endpoints**

| Method  | Endpoint                           | Description                                                    |
|---------|------------------------------------|----------------------------------------------------------------|
| **GET** | `/reclamations/hello`              | Test endpoint to say "Hello" from the Reclamation Service.     |
| **POST**| `/reclamations`                    | Add a new reclamation (complaint).                             |
| **GET** | `/reclamations`                    | Retrieve all reclamations.                                     |
| **GET** | `/reclamations/{id}`               | Retrieve a specific reclamation by ID.                         |
| **PUT** | `/reclamations/{id}`               | Update an existing reclamation by ID.                          |
| **DELETE**| `/reclamations/{id}`             | Delete a reclamation by ID.                                    |
| **GET** | `/reclamations/{id}/qr`            | Generate a QR code for a specific reclamation.                 |
| **GET** | `/reclamations/qr/{filename:.+}`   | Retrieve the QR code image for a reclamation.                  |
| **GET** | `/reclamations/pdf`                | Download all reclamations as a PDF document.                   |

---

### **QR Code Generation**

- Each reclamation can generate a unique QR code that includes details such as the reclamation ID, description, order ID, status, and client email.
- The QR code can be accessed through the endpoint `GET /reclamations/{id}/qr`.
- You can retrieve the generated QR code image by visiting `GET /reclamations/qr/{filename}`.

---

## ⚙️ **Configuration**

### `application.properties`

```properties
# Reclamation Service Configuration

# Application Name and Port
spring.application.name=ReclamationService
server.port=8084

# H2 Database Configuration (for development and testing)
spring.h2.console.enabled=true
spring.h2.console.path=/h2
spring.datasource.url=jdbc:h2:file:./Database/Data/reclamation
spring.datasource.username=chaher
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update

# Eureka Service Registration
eureka.client.register-with-eureka=true
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/

# Spring Cloud Config
spring.cloud.config.enabled=true
spring.config.import=optional:configserver:http://localhost:8888
```

### **Database Configuration**

- The service uses an **H2 database** for development purposes. You can access the H2 database console at `http://localhost:8084/h2` when the application is running.

### **Eureka & Config Server**

- The application registers with **Eureka** for service discovery.
- The **Spring Cloud Config Server** loads externalized configurations.

---

## 📁 **Project Structure**

```plaintext
Reclamation_Service/
├── src/
│   ├── main/java/
│   │   └── tn/esprit/microservice/reclamation_service/
│   │       ├── ReclamationRestAPI.java
│   │       └── ... (other classes)
│   └── resources/
│       └── application.properties
├── Dockerfile
├── pom.xml
└── README.md  ← You are here
```

---

## 🧪 **Testing the Service**

You can test the basic functionality by using the following `curl` command:

```bash
curl http://localhost:8084/reclamations/hello
```

This should return a simple message like:  
`"Hello from Reclamation Service!"`

---

## 🧑‍💻 **Author**

**Dridi Chaher** – Intern at Banque de Tunisie

- GitHub: [chahe-dridi](https://github.com/chahe-dridi)

This microservice is part of the **distributed-web-application** project.

---

### 🚀 **Contributing**

Feel free to fork this repository and submit issues or pull requests if you'd like to contribute.

---

### License

Distributed under the MIT License. See `LICENSE` for more information.
