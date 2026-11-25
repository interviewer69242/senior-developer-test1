FROM gradle:8.12.1-jdk21 AS build
USER root
WORKDIR /app
COPY --chown=gradle:gradle settings.gradle build.gradle ./
COPY --chown=gradle:gradle src src
RUN chown gradle:gradle /app
USER gradle
RUN gradle --no-daemon bootJar
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
