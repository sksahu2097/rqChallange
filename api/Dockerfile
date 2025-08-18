# ---------- Build Stage ----------
FROM gradle:8.7-jdk17 AS builder
WORKDIR /app

## Copy gradle wrapper and root configs
 COPY gradlew* settings.gradle ./
 COPY gradle ./gradle
 COPY buildSrc ./buildSrc

 # Copy only the api module build files
 COPY api/build.gradle ./api/
 COPY api/src ./api/src


# Build only the "api" module
RUN ./gradlew :api:clean :api:build --no-daemon

# ---------- Runtime Stage ----------
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# Copy api JAR
COPY --from=builder /app/api/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
