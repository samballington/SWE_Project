# -------- Build Stage --------
FROM maven:3.8-openjdk-17 AS build
WORKDIR /app

# Copy the Maven project (pom.xml + src) from the obs subdirectory
COPY obs/pom.xml ./
COPY obs/src ./src

# Download dependencies and build the application JAR
RUN mvn clean package -DskipTests

# -------- Runtime Stage --------
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy the generated JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"] 