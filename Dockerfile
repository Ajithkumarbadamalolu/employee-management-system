# Step 1: Use a lightweight Java runtime
FROM eclipse-temurin:17-jdk-alpine

# Step 2: Set the working directory inside the container
WORKDIR /app

# Step 3: Copy the JAR file from your target folder to the container
# Maven names the jar 'ems-backend-0.0.1-SNAPSHOT.jar' by default
COPY target/*.jar app.jar

# Step 4: Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]