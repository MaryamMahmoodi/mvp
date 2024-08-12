# URL Shortener API

This project is a simple URL shortener API built with offering basic functionality to shorten URLs and resolve them back to their original form.

## Table of Contents

1. [Technologies Used](#technologies-used)
2. [Project Structure](#project-structure)
3. [API Endpoints](#api-endpoints)
4. [Exception Handling](#exception-handling)
5. [Unit Testing](#unit-testing)
6. [How to Run](#how-to-run)
7. [Dockerization](#dockerization)
8. [Swagger Documentation](#swagger-documentation)


## Technologies Used

- **Java 17**: The core programming language.
- **Spring Boot 3.3.2**: Framework used to build the REST API.
- **H2 Database**: An in-memory database used for development and testing.
- **Maven**: Dependency management and project building tool.
- **Swagger**: Used for API documentation and testing.
- **JUnit 5**: For writing and running unit tests.
- **Docker**: To containerize the application for easy deployment and scalability.
- **Jackson ObjectMapper**: For JSON parsing in tests.
- **Lombok**: Used to reduce code by generating getters, setters, constructors at compile time.


## Project Structure

The project is organized into several packages:

- **controller**: Contains the REST controllers that handle incoming HTTP requests.
- **model**: Contains the entity classes and DTOs.
   - **entity**: Defines the `Url` entity class.
   - **dto**: Define a DTO that represents the structure of the JSON payload expected by the API.
   - **exception**: Custom exception classes and their handlers.
   - **service**: Contains service layer logic.
      - **contract**: Contains service interfaces and repository interfaces.
      - **imp**: Contains implementations of service interfaces.
   - **MvpApplication** main class.

## API Endpoints

- **POST /api/url/shorten**: Accepts a JSON payload with a URL to shorten. Returns the shortened URL code.
- **GET /api/url/{shortCode}**: Resolves the shortened URL code to its original URL.

## Exception Handling

Custom exceptions are used to handle specific errors such as:

- **UrlNotFoundException**: Thrown when a URL with a given shortcode does not exist in the database.
- **DuplicateUrlException**: Thrown when an attempt is made to shorten a URL that has already been shortened.

These exceptions are caught and handled by a global exception handler, which returns appropriate HTTP status codes and messages.

## Unit Testing

Unit tests are written using **JUnit 5** to ensure the correctness of the service and controller layers. The tests cover:

- **URL shortening and resolving**: Ensuring that the API can shorten URLs and resolve them correctly.
- **Exception handling**: Verifying that custom exceptions are thrown and handled appropriately.

## How to Run

### 1. Clone the repository:
```bash
git clone https://github.com/MaryamMahmoodi/mvp.git
cd mvp
```
### 2. Dockerization
#### Build the Docker Image
The application is using **Docker**. The `Dockerfile` is located in the root directory and defines the steps to build a Docker image for the application.

```bash
docker build -t mvp-app .
```
#### Run the Docker Container

```bash
docker run -p 8080:8080 mvp-app
```

## Swagger Documentation

Swagger is used to document and test the API endpoints. You can access the Swagger UI by navigating to:
```http://localhost:8080/swagger-ui/index.html```

## Testing the Application
   You can test the application using Swagger UI or Postman:

#### Using Swagger UI
Navigate to http://localhost:8080/swagger-ui.html.
Use the available endpoints to test URL shortening and resolution.
#### Using Postman
- Shorten URL: Send a POST request to ```http://localhost:8080/api/url/shorten``` with the request body containing a JSON object,
e.g., {"url": "https://www.example.com"}.

- Resolve URL: Send a GET request to ```http://localhost:8080/api/url/{shortCode}```, replacing {shortCode} with the actual short code received from the shorten URL response.
#### Running Tests (Optional)
```bash
./mvnw test
```