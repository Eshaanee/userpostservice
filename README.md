# UserPostAPI

A fully containerized Spring Boot REST API for managing Users and Posts, built with:

- Spring Boot
- Docker
- AWS App Runner (fully managed CI/CD pipeline)
- GitHub (source control)

---

# Features

- Full CRUD APIs for Users and Posts
- Custom Exception Handling
- Spring Retry with Recovery mechanism
- Docker-based build & deployment
- Fully automated CI/CD pipeline using AWS App Runner and GitHub
- Clean code separation across service layers

---

# Project Architecture & Code Structure

The application is designed following **clean layered architecture** principles to ensure:

| Package | Responsibility |
| ------- | -------------- |
| `controller` | Handles HTTP requests & exposes REST APIs |
| `service` | Business logic layer (orchestration logic happens here) |
| `repository` | Data access layer (In-memory data storage implementation) |
| `model` | POJO classes representing domain entities (`User`, `Post`) |
| `exception` | Centralized exception handling and custom exception definitions |
| `UserPostApplication.java` | Application bootstrap class (Spring Boot entry point) |

---

# Layered Design Summary

- **Controller Layer**: REST APIs for user and post management.
- **Service Layer**: Core business logic, retries, recovery, orchestration.
- **Repository Layer**: In-memory data storage using Java Maps.
- **Model Layer**: Domain objects for data transfer.
- **Exception Layer**: Custom exceptions + centralized error handling.
- **Entry Point**: `UserPostApplication` boots the Spring context.

- ---

# Deployment & CI/CD Pipeline

> There is **no local build or manual deployment required**.

# Fully automated pipeline:

1)Code is stored on **GitHub**  
2️)**AWS App Runner** is integrated directly with GitHub:
- Monitors repo for changes
- Automatically pulls code
- Builds Docker image from provided Dockerfile
- Deploys updated version seamlessly

3️) No manual Docker commands or infrastructure configuration needed.

---

# Benefits:

-  End-to-end CI/CD automated
-  Zero downtime deployments
-  No server maintenance
-  SSL & DNS handled automatically by AWS
-  Scales automatically

# Dockerfile (Used directly by AWS App Runner)

```dockerfile
FROM amazoncorretto:17
# Define an argument for the path to the JAR file created by Maven.
# This makes the Dockerfile more flexible.
ARG JAR_FILE=target/*.jar

# Copy the JAR file from the build context (where CodeBuild runs) into the container.
# It is renamed to "application.jar" for a consistent name.
COPY ${JAR_FILE} application.jar

# Inform Docker that the container listens on port 8080 at runtime.
# This is the default port for Spring Boot web applications.
EXPOSE 8080

# The command to run when the container starts.
# This executes the Spring Boot application.
ENTRYPOINT ["java", "-jar", "application.jar"]

---

** API Endpoints**
User APIs
| Method | Endpoint    | Description    |
| ------ | ----------- | -------------- |
| POST   | /users      | Create a user  |
| GET    | /users/{id} | Get user by ID |
| GET    | /users      | Get all users  |
| PUT    | /users/{id} | Update user    |
| DELETE | /users/{id} | Delete user    |

Post APIs
| Method | Endpoint    | Description    |
| ------ | ----------- | -------------- |
| POST   | /posts      | Create a post  |
| GET    | /posts/{id} | Get post by ID |
| GET    | /posts      | Get all posts  |
| PUT    | /posts/{id} | Update post    |
| DELETE | /posts/{id} | Delete post    |


** Application URL**
https://qsmgq6qpcg.us-east-2.awsapprunner.com/users
https://qsmgq6qpcg.us-east-2.awsapprunner.com/posts




