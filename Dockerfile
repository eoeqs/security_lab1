FROM openjdk:17-jdk-slim

RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY target/secure-api-1.0.0.jar app.jar

RUN groupadd -r spring && useradd -r -g spring spring
USER spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]