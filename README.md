# UserPostAPI

A fully containerized Spring Boot REST API for managing Users and Posts, built with:

- Spring Boot
- Docker
- AWS App Runner (fully managed CI/CD pipeline)
- GitHub (source control)

---

** Application URL**
https://qsmgq6qpcg.us-east-2.awsapprunner.com/users
https://qsmgq6qpcg.us-east-2.awsapprunner.com/posts

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

---

# Fully automated pipeline:

**CI/CD is implemented via AWS CodePipeline** which has 4 steps:
1. Source code pull stage - which pulls the code automatically on changes from GitHub repo - main branch
2. Build using AWS CodeBuild - Builds the Maven jar package
3. Docker image using ECR - Creates the docker image  in AWS ECR
4. Deployment of service using AWS App Runner - Hosts the app remotely via deploying the generated ECR image

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

## API Endpoints

### Users API Endpoints (`/users`)

This resource manages user data.

#### 1. Get All Users

- **Endpoint:** `GET /users`
- **Description:** Retrieves a list of all users.
- **Request Body:** None.
- **Success Response:**
  - Code: `200 OK`
  - Content:

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
2. Create a New User
Endpoint: POST /users

Description: Creates a new user.

Request Body:

```json
{
  "name": "Peter Jones",
  "email": "peter.jones@example.com"
}
Success Response:

Code: 201 Created

```json

{
  "id": 3,
  "name": "Peter Jones",
  "email": "peter.jones@example.com"
}
3. Get a Single User
Endpoint: GET /users/{id}

Description: Retrieves a single user by their unique ID.

Request Body: None.

Success Response:

Code: 200 OK

Content: The requested user object.

Error Response:

Code: 404 Not Found

Content:

```json
{
  "error": "User with id 99 not found."
}
4. Update a User
Endpoint: PUT /users/{id}

Description: Updates the name and email of an existing user.

Request Body:

```json

{
  "name": "Peter T. Jones",
  "email": "p.jones@example.com"
}
Success Response:

Code: 200 OK

Content: The fully updated user object.

Error Response:

Code: 404 Not Found

5. Delete a User
Endpoint: DELETE /users/{id}

Description: Deletes a user by their unique ID.

Request Body: None.

Success Response:

Code: 204 No Content

Error Response:

Code: 404 Not Found

Posts API Endpoints (/posts)
This resource manages posts, which are associated with users.

1. Get All Posts
Endpoint: GET /posts

Description: Retrieves a list of all posts.

Request Body: None.

Success Response:

Code: 200 OK

Content:

```json

[
  {
    "id": 1,
    "title": "My First Post",
    "content": "This is the content.",
    "userId": 1
  }
]
2. Create a New Post
Endpoint: POST /posts

Description: Creates a new post associated with an existing user.

Request Body:

```json

{
  "title": "Another Post",
  "content": "More content here.",
  "userId": 2
}
Success Response:

Code: 201 Created

Content: The newly created post object with its server-assigned id.

Error Response:

Code: 500 Internal Server Error

Content:

```json

{
  "error": "Cannot create post. User with id 99 does not exist."
}
3. Get a Single Post
Endpoint: GET /posts/{id}

Description: Retrieves a single post by its unique ID.

Request Body: None.

Success Response:

Code: 200 OK

Error Response:

Code: 404 Not Found

4. Update a Post
Endpoint: PUT /posts/{id}

Description: Updates the title, content, and user association of an existing post.

Request Body: JSON object with updated post details.

Success Response:

Code: 200 OK

Error Response:

Code: 404 Not Found if post ID does not exist.

Code: 500 Internal Server Error if userId does not exist.

5. Delete a Post
Endpoint: DELETE /posts/{id}

Description: Deletes a post by its unique ID.

Request Body: None.

Success Response:

Code: 204 No Content

Error Response:

Code: 404 Not Found

Testing Approach
Unit Testing
Each service class (UserServiceImpl, PostServiceImpl) tested independently.

Used mocked repositories to verify business logic.

Integration Testing
Full end-to-end API behavior tested using embedded server.

Verified correct status codes, request, and response payloads.

Tested retry logic and exception handling.

Deployment Process
Local Development
Built complete Spring Boot REST API.

Verified functionality locally using Postman for CRUD operations.

Version Control
Initialized local Git repository and pushed source code to GitHub:

bash
git init
git remote add origin <github-repo-url>
git push origin main

AWS App Runner Deployment
On AWS Console:

CI/CD is implemented via AWS CodePipeline which has 4 steps:
1. Source code pull stage - which pulls the code automatically on changes from GitHub repo - main branch
2. Build using AWS CodeBuild - Builds the Maven jar package
3. Docker image using ECR - Creates the docker image  in AWS ECR
4. Deployment of service using AWS App Runner - Hosts the app remotely via deploying the generated ECR image

Tools Used
Spring Boot (Java 17)

Docker

GitHub

AWS App Runner (fully managed deployment service)

# How to Run Locally (from ZIP file)

Follow these steps to run the application locally after receiving the code as a ZIP file:

# Unzip the Code

Extract the contents of the ZIP file to your desired directory:

```bash
unzip userpostservice.zip
cd extracted-folder-name

**Install Java**
Make sure Java 17 is installed. Verify with:

java -version

**Build & Run the Application**
If you're using Maven:

./mvnw spring-boot:run

Or if Maven is already installed globally:
mvn spring-boot:run

**Access the Application**
Once started, the API will be available at:

http://localhost:8080/users

http://localhost:8080/posts























