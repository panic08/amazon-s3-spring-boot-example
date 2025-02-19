# Amazon S3 Spring Boot Example

This is a minimal example of using Amazon S3 in a Spring Boot application. It demonstrates how to integrate Amazon S3 functionality with Spring Boot, using a local Minio instance as the S3 service.

## Features

- Simple Spring Boot application integrated with Amazon S3 using Minio.
- Uses H2 database for local storage.
- Configurable via `docker-compose` for easy setup and testing.

## Project Structure

- **Dockerfile**: Defines the container image for the Spring Boot application.
- **test-docker-compose.yml**: Defines services for the Spring Boot app and Minio.
- **Spring Boot dependencies**: Includes necessary dependencies for AWS SDK, JPA, H2, Lombok, and MapStruct.

## Setup Instructions

### 1. Clone the repository:

```bash
git clone https://github.com/panic08/amazon-s3-spring-boot-example.git
cd amazon-s3-spring-boot-example
```

### 2. Build and start the services using Docker Compose:

```bash
docker-compose -f test-docker-compose.yml up --build
```

This command will build and start the Spring Boot application along with the Minio service.

### 3. Access the services:
- Spring Boot app: Available at [http://localhost:9000](http://localhost:9000)
- Minio S3 console: Available at [http://localhost:9001](http://localhost:9001)

    - Username: `minioadmin`
    - Password: `minioadmin`

### 4. Configure your application:
You can customize the application settings such as AWS region, access keys, and endpoints by modifying the environment variables in the `test-docker-compose.yml` file:
- `AWS_S3_REGION`: Set to your desired AWS region (default: `us-east-1`).
- `AWS_S3_ACCESS_KEY` and `AWS_S3_SECRET_KEY`: Set to your Minio credentials.
- `AWS_S3_ENDPOINT`: Set the endpoint for Minio (default: `http://minio:9000`).

### 5. Dependencies:
The project includes the following dependencies:
- `spring-boot-starter-data-jpa`
- `spring-boot-starter-web`
- `software.amazon.awssdk:s3`
- `lombok`
- `mapstruct`
- `h2database`

You can add additional dependencies as needed for your use case.

## Running Tests
The project includes tests based on `spring-boot-starter-test` and can be executed using the following command:

```bash
./mvnw test
```
