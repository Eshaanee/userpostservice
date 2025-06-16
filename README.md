# User-Post REST API

A Spring Boot-based REST API to manage Users and Posts with full CRUD functionality, containerized using Docker and deployed via AWS App Runner with CI/CD integration.

---

## Table of Contents

* [API Overview](#api-overview)
* [Project Architecture & Code Structure](#project-architecture--code-structure)
* [How to Run Locally (from ZIP file)](#how-to-run-locally-from-zip-file)
* [API Endpoints](#api-endpoints)

  * [Users API](#users-api-endpoints-users)
  * [Posts API](#posts-api-endpoints-posts)
* [Application URL](#application-url)
* [Testing Approach](#testing-approach)
* [Deployment Process](#deployment-process)

  * [Local Development](#local-development)
  * [Containerization (Dockerfile)](#containerization-dockerfile)
  * [Version Control](#version-control)
  * [AWS App Runner Deployment](#aws-app-runner-deployment)
  * [CI/CD Automation](#cicd-automation)
* [Tools Used](#tools-used)

---

## API Overview

The UserPostAPI is a RESTful API that allows managing users and posts. The system supports:

* CRUD operations for both users and posts.
* Retry and recovery mechanisms while creating users.
* Custom exception handling for all major error scenarios.
* In-memory persistence for simplicity.
* Fully containerized and deployed using AWS App Runner and GitHub CI/CD.

---

## Project Architecture & Code Structure

The application is designed following clean layered architecture principles to ensure:

* Code separation
* Scalability
* Maintainability
* Testability

### Code Segregation

| Package                    | Responsibility                                                  |
| -------------------------- | --------------------------------------------------------------- |
| `controller`               | Handles HTTP requests & exposes REST APIs                       |
| `service`                  | Business logic layer (orchestration logic happens here)         |
| `repository`               | Data access layer (In-memory data storage implementation)       |
| `model`                    | POJO classes representing domain entities (User, Post)          |
| `exception`                | Centralized exception handling and custom exception definitions |
| `UserPostApplication.java` | Application bootstrap class (Spring Boot entry point)           |

### Benefits of This Design

* Clear separation of concerns (Controller - Service - Repository).
* Easy to maintain and extend (Database addition requires minimal changes).
* Simplifies unit testing and mocking layers.
* Makes retry, recovery, and exception handling modular.
* Aligned with Spring Boot best practices.

### API Design Summary

* Follows REST principles (resource-based URLs, proper HTTP methods).
* All inputs are handled using `@Valid` and `@RequestBody` annotations.
* Exception handling is centralized through `RestExceptionHandler`.
* `ResponseEntity` is used to return appropriate status codes and responses.
* Clean separation of controller, service, and repository layers.

---

## How to Run Locally (from ZIP file)

### Unzip the Code

```bash
unzip your-file-name.zip
cd extracted-folder-name
```

### Install Java

Make sure Java 17 is installed.

```bash
java -version
```

### Install Maven

Check if Maven is installed:

```bash
mvn -version
```

If Maven is not installed, install it accordingly.

### Run Directly from Source Code (No JAR needed)

```bash
mvn spring-boot:run
```

or (if `mvnw` is available):

```bash
./mvnw spring-boot:run
```

### Access the Application

Once running:

* `http://localhost:8080/users`
* `http://localhost:8080/posts`

---

## API Endpoints

### Users API Endpoints (`/users`)

#### 1. Get All Users

* **Endpoint:** GET /users
* **Description:** Retrieves a list of all users.
* **Request Body:** None.
* **Success Response:**

  * Code: 200 OK
  * Content:

```json
[
  {
    "id": 1,
    "name": "John Doe",
    "email": "john.doe@example.com"
  },
  {
    "id": 2,
    "name": "Jane Smith",
    "email": "jane.smith@example.com"
  }
]
```

* **Error Response:** None typical for this endpoint.

#### 2. Create a New User

* **Endpoint:** POST /users
* **Description:** Creates a new user.
* **Request Body:**

```json
{
  "name": "Peter Jones",
  "email": "peter.jones@example.com"
}
```

* **Success Response:**

  * Code: 201 Created
  * Content:

```json
{
  "id": 3,
  "name": "Peter Jones",
  "email": "peter.jones@example.com"
}
```

* **Error Response:** None typical for this simple version.

#### 3. Get a Single User

* **Endpoint:** GET /users/{id}
* **Description:** Retrieves a single user by their unique ID.
* **Request Body:** None.
* **Success Response:**

  * Code: 200 OK
  * Content: The requested user object.
* **Error Response:**

  * Code: 404 Not Found
  * Content:

```json
{
  "error": "User with id 99 not found."
}
```

#### 4. Update a User

* **Endpoint:** PUT /users/{id}
* **Description:** Updates the name and email of an existing user.
* **Request Body:**

```json
{
  "name": "Peter T. Jones",
  "email": "p.jones@example.com"
}
```

* **Success Response:**

  * Code: 200 OK
  * Content: The fully updated user object.
* **Error Response:**

  * Code: 404 Not Found

#### 5. Delete a User

* **Endpoint:** DELETE /users/{id}
* **Description:** Deletes a user by their unique ID.
* **Request Body:** None.
* **Success Response:**

  * Code: 204 No Content
* **Error Response:**

  * Code: 404 Not Found

---

### Posts API Endpoints (`/posts`)

#### 1. Get All Posts

* **Endpoint:** GET /posts
* **Description:** Retrieves a list of all posts.
* **Request Body:** None.
* **Success Response:**

  * Code: 200 OK
  * Content:

```json
[
  {
    "id": 1,
    "title": "My First Post",
    "content": "This is the content.",
    "userId": 1
  }
]
```

#### 2. Create a New Post

* **Endpoint:** POST /posts
* **Description:** Creates a new post associated with an existing user.
* **Request Body:**

```json
{
  "title": "Another Post",
  "content": "More content here.",
  "userId": 2
}
```

* **Success Response:**

  * Code: 201 Created
  * Content: The newly created post object with its server-assigned id.
* **Error Response:**

  * Code: 500 Internal Server Error (as implemented in our service layer)
  * Content:

```json
{
  "error": "Cannot create post. User with id 99 does not exist."
}
```

#### 3. Get a Single Post

* **Endpoint:** GET /posts/{id}
* **Description:** Retrieves a single post by its unique ID.
* **Request Body:** None.
* **Success Response:**

  * Code: 200 OK
* **Error Response:**

  * Code: 404 Not Found

#### 4. Update a Post

* **Endpoint:** PUT /posts/{id}
* **Description:** Updates the title, content, and user association of an existing post.
* **Request Body:** JSON object with updated post details.
* **Success Response:**

  * Code: 200 OK
* **Error Response:**

  * Code: 404 Not Found if the post ID does not exist.
  * Code: 500 Internal Server Error if the userId does not exist.

#### 5. Delete a Post

* **Endpoint:** DELETE /posts/{id}
* **Description:** Deletes a post by its unique ID.
* **Request Body:** None.
* **Success Response:**

  * Code: 204 No Content
* **Error Response:**

  * Code: 404 Not Found

---

## Application URL

* `https://qsmgq6qpcg.us-east-2.awsapprunner.com/users`
* `https://qsmgq6qpcg.us-east-2.awsapprunner.com/posts`

---

## Testing Approach

### Unit Testing

* The `UserServiceImplTest` class tests service layer business logic.
* Used Mockito framework to mock repository layer.
* Test scenarios included:

  * Fetching all users.
  * Fetching user by ID (valid and invalid).
  * Exception handling (`ResourceNotFoundException`).
* Assertions performed using JUnit and AssertJ.

### Integration Testing

* The `UserControllerTest` class tests the REST controller layer.
* Used `@WebMvcTest` for controller-only testing.
* Used MockMvc for HTTP request-response simulation.
* Test scenarios included:

  * Creating a user (`POST /users`).
  * Fetching a user (`GET /users/{id}`).
* ObjectMapper used for JSON serialization and deserialization.
* Service layer was mocked using Mockito.

### Tools & Libraries Used

* JUnit 5
* Mockito
* Spring Boot Test
* MockMvc
* ObjectMapper (Jackson)

---

## Deployment Process

### Local Development

* Spring Boot REST API developed & tested locally.

### Containerization (Dockerfile)

Here’s the Dockerfile used for containerization:

```dockerfile
FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

### Version Control

```bash
git init
git remote add origin <github-repo-url>
git push origin main
```

### AWS App Runner Deployment

* Source Code Repository: GitHub
* Branch: `main`
* Runtime: Dockerfile
* Fully managed build & deployment by App Runner

### CI/CD Automation

* App Runner automatically redeploys on every commit pushed to GitHub.

### CI/CD Pipeline Details

CI/CD is implemented via AWS CodePipeline which has 4 steps:

1. **Source code pull stage** - Pulls the code automatically on changes from GitHub repo (main branch).
2. **Build using AWS CodeBuild** - Builds the Maven JAR package.
3. **Docker image using ECR** - Creates the Docker image in AWS ECR.
4. **Deployment of service using AWS App Runner** - Hosts the app remotely by deploying the generated ECR image.

---

## Tools Used

* Spring Boot (Java 17)
* Maven
* Docker
* GitHub
* AWS App Runner

---
