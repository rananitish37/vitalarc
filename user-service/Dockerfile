# --- Build stage ---
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests -B

# --- Runtime stage ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Run as non-root - basic container security hygiene
RUN addgroup -S spring && adduser -S spring -G spring
USER spring
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
