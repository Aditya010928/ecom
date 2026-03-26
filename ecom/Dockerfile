FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copy jar
COPY target/ecom-0.0.1-SNAPSHOT.jar app.jar

# External config location set
ENV SPRING_CONFIG_LOCATION=file:/config/

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]