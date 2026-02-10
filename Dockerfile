11111111111111111111111111111111111111111111
4444444444444444444444444444444
# Copy Maven files first (for dependency caching)
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY src src

# Build the Spring Boot jar
RUN mvn clean package -DskipTests


# Stage 2: Run the application
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/target/*.war app.war

# Expose Spring Boot port
EXPOSE 8080

# Run application
ENTRYPOINT ["java", "-jar", "app.war"]
