# Spring Boot Audit System

This project is a Spring Boot application designed to manage and audit a `Person` entity. It includes basic CRUD operations, audit logging of every request and response, and stores audit logs in a MySQL database. The application also supports Docker for containerization.

## Features

- **Person Management**: CRUD operations for managing `Person` entities (create, read, update, delete).
- **Audit Logging**: Logs every request and response for auditing purposes.
- **Database**: Stores `Person` entities and audit logs in a MySQL database.
- **Dockerized**: The application is containerized using Docker for easy deployment.

## Requirements

- Java 11 or higher
- Docker
- MySQL (Docker containerized or local installation)

## Setup

### 1. Clone the repository

```bash
git clone https://git.gridsig.com/murugapandian/springboot-audits.git
cd springboot-audits
'''
```

### 2. Set up MySQL with Docker

You can use Docker to run the MySQL database with the following command:

```bash
docker-compose up -d
```
This will create and run a MySQL container with the following environment variables:

- **MYSQL_ROOT_PASSWORD**: root
- **MYSQL_DATABASE**: aldb
- **MYSQL_USER**: al_user
- **MYSQL_PASSWORD**: al_pwd

It also includes a volume to load the initial `init.sql` script into the database (used for database schema initialization).

### 3. Configure Application

The application is pre-configured to work with the `aldb` database running in the MySQL container. Ensure that the `application.properties` (or `application.yml`) is correctly configured for your database, if necessary.

Example configuration for `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3500/aldb
spring.datasource.username=al_user
spring.datasource.password=al_pwd
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

### 4. Run the Application

To run the application locally, execute:

```bash
./mvnw spring-boot:run
```
Alternatively, if you are using an IDE like IntelliJ IDEA or Eclipse, you can run the application directly from the IDE.

### 5. Access the API

Once the application is up and running, you can access the API at:

- **GET** `/person/list` - List all persons (optional filters).
- **POST** `/person/save` - Save a new person.
- **POST** `/person/update` - Update an existing person.
- **GET** `/person/{id}` - Retrieve a person by ID.
- **DELETE** `/person/{id}` - Delete a person by ID.
- **GET** `/` - Check the service status.

### 6. Audit Logs

The application logs all requests and responses for audit purposes. These logs are saved in the MySQL database as part of the application's audit logging functionality.

### 7. Docker Compose Configuration

The `docker-compose.yml` is set up to run MySQL in a container. The `logs/` directory is ignored by Git as it stores runtime logs that are not meant to be tracked by version control.

#### Example Docker Compose File

```yaml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    container_name: al_db
    ports:
      - "3500:3306"
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: aldb
      MYSQL_USER: al_user
      MYSQL_PASSWORD: al_pwd
    volumes:
      - ./logs:/app/logs
      - ./src/main/resources/init.sql:/docker-entrypoint-initdb.d/init.sql
    networks:
      - al_network

volumes:
  mysql_data:

networks:
  al_network:
    driver: bridge
```

## Conclusion

This application is a fully functional Spring Boot-based system for managing and auditing `Person` entities, with Dockerized MySQL for database management. The system tracks every request and response for audit purposes and offers a robust, scalable solution for managing entities.

