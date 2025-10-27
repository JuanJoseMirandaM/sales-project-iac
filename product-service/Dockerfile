FROM openjdk:17-jdk-slim

WORKDIR /app

COPY . .

RUN chmod +x ./mvnw && ./mvnw clean package -DskipTests

EXPOSE 8082

# Set MongoDB URI environment variable
ENV MONGODB_URI="mongodb+srv://jjmirandam_db_user:BJyZjBN4naetrmYU@clustersoe.bcqm3lu.mongodb.net/app?retryWrites=true&w=majority&appName=ClusterSoe"

CMD ["sh", "-c", "java -jar target/product-service-1.0.0.jar --spring.data.mongodb.uri=${MONGODB_URI}"]
