# build
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn -q -DskipTests package

# run
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/my-app-name-1.0-SNAPSHOT.jar app.jar
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS=""
EXPOSE 8080
CMD ["sh","-c","java $JAVA_OPTS -jar app.jar"]
