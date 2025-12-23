FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY target/bank-rest-0.0.1-SNAPSHOT.jar app.jar

COPY docs /docs

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
