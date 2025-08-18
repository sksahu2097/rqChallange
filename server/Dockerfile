# ---- Build Stage ----
FROM gradle:8.7-jdk17 AS builder

WORKDIR /app

# Copy Gradle wrapper and shared configs
COPY gradlew* settings.gradle ./
COPY gradle ./gradle
COPY buildSrc ./buildSrc

# Copy server-specific build files and source
COPY server/build.gradle ./server/
COPY server/src ./server/src

# Build only the server module
RUN ./gradlew :server:build -x test --no-daemon

# ---- Runtime Stage ----
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Copy the built jar from builder stage
COPY --from=builder /app/server/build/libs/*.jar app.jar

EXPOSE 8112

ENTRYPOINT ["java", "-jar", "app.jar"]
