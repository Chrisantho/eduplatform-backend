FROM openjdk:27-ea-8-jdk-slim-trixie

WORKDIR /app

COPY target/eduplatform-backend-1.0.0.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
