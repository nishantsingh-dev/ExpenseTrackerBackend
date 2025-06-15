FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY expensetracker/pom.xml .
RUN mvn dependency:go-offline
COPY expensetracker/src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /app/target/expensetracker-0.0.1-SNAPSHOT.jar expensetracker.jar
ENTRYPOINT ["java", "-jar", "-Dserver.port=${PORT}", "expensetracker.jar"]
