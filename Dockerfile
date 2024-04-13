FROM maven:3.8.4-openjdk-17-slim as builder

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src /app/src

RUN mvn -B clean package -DskipTests

FROM openjdk:17-slim

COPY --from=builder /app/target/*.jar /app/app.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/app.jar"]
