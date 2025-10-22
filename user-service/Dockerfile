FROM openjdk:17-jdk-slim

WORKDIR /app

COPY . .

RUN chmod +x ./mvnw && ./mvnw clean package -DskipTests

EXPOSE 8081

CMD ["java", "-jar", "target/user-service-1.0.0.jar"]
