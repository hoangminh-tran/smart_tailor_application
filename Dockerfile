FROM maven:3.9.9-amazoncorretto-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn package -DskipTests

FROM amazoncorretto:17.0.12
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 6968
ENV SPRING_PROFILES_ACTIVE prod
ENTRYPOINT ["java", "-jar", "app.jar"]
