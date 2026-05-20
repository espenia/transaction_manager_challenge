# Use a base image with a JDK (Java Development Kit)
FROM eclipse-temurin:21-jdk-jammy AS build

# Set the working directory in the container
WORKDIR /app

COPY build.gradle gradlew /app/
COPY gradle /app/gradle
RUN ./gradlew build || return 0

# Copy the source code of your Spring Boot application into the container
COPY . /app

# Build the Spring Boot application inside the container
RUN ./gradlew build -x test

# Use a base image with a JRE (Java Runtime Environment)
FROM eclipse-temurin:21-jdk-jammy

# Set the working directory in the container
WORKDIR /app

# Copy the compiled Spring Boot JAR file into the container from the build stage
COPY --from=build /app/build/libs/transaction_manager_challenge-0.0.1-SNAPSHOT.jar /app/transaction_manager_challenge-0.0.1-SNAPSHOT.jar


# Expose the port your Spring Boot application is running on (default is 8080)
EXPOSE 8080

# Command to run your Spring Boot application
CMD ["java", "-jar", "/app/transaction_manager_challenge-0.0.1-SNAPSHOT.jar"]
