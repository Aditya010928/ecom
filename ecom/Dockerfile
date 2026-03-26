FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY target/ecom-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar", "--spring.config.additional-location=file:/config/"]