# Build stage
FROM maven:3.8.6-openjdk-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM openjdk:17-jdk-slim
WORKDIR /app

# Install curl untuk health check
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Copy JAR dari build stage
COPY --from=builder /app/target/*.jar app.jar

# Buat user non-root untuk keamanan
RUN groupadd -r spring && useradd -r -g spring spring
USER spring

# Expose port
EXPOSE 8080

# Set environment variables default
ENV SPRING_PROFILES_ACTIVE=prod

# Entrypoint untuk menjalankan aplikasi
ENTRYPOINT ["java", "-jar", "/app/app.jar"]