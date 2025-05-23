# Use official OpenJDK 21 JDK slim image
FROM openjdk:21-jdk-slim
LABEL authors="Champ Sukra"

# Set a working directory in the container
WORKDIR /app

# Copy the JAR from the target directory into the image
# (Make sure to build with: ./gradlew build)
COPY build/libs/address-book-service-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port (Spring Boot default)
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
