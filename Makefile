# Variables
COMPOSE_FILE=docker-compose.yml
MYSQL_SERVICE=mysql
SPRING_APP=your-spring-boot-application.jar
JAVA_CMD=java -jar

# Default goal
.DEFAULT_GOAL := help

# Commands
help:  ## Display this help
	@echo "Usage: make [target]"
	@echo ""
	@echo "Targets:"
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "  %-20s %s\n", $$1, $$2}'

up:  ## Start MySQL container using docker-compose
	@echo "Starting MySQL container..."
	docker-compose -f $(COMPOSE_FILE) up -d

down:  ## Stop and remove MySQL container
	@echo "Stopping MySQL container..."
	docker-compose -f $(COMPOSE_FILE) down

logs:  ## Show logs for MySQL container
	@echo "Showing logs for MySQL container..."
	docker logs $(MYSQL_SERVICE)

restart: down up ## Restart MySQL container

clean:  ## Stop MySQL and remove all associated volumes
	@echo "Cleaning MySQL setup..."
	docker-compose -f $(COMPOSE_FILE) down --volumes

build:  ## Build the Spring Boot application (assumes Maven or Gradle)
	@echo "Building Spring Boot application..."
	./mvnw clean package # Replace with ./gradlew build if using Gradle

run: build ## Run the Spring Boot application
	@echo "Running Spring Boot application..."
	$(JAVA_CMD) target/$(SPRING_APP)

stop:  ## Stop the Spring Boot application
	@echo "Stopping Spring Boot application..."
	@pkill -f "$(JAVA_CMD)" || echo "No running application found."

status:  ## Check status of MySQL container
	@docker-compose -f $(COMPOSE_FILE) ps

