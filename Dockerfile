FROM gradle:jdk17-jammy AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon -x test

FROM openjdk:17.0.2-jdk
EXPOSE 8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/svq-backend-0.0.1-SNAPSHOT.jar /app/spring-boot-application.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "/app/spring-boot-application.jar"]