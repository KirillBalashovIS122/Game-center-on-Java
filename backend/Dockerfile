FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn package -DskipTests

FROM eclipse-temurin:17
WORKDIR /app
COPY --from=build /app/target/gamecenter-0.0.1-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]
