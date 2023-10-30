FROM gradle:8.4.0-jdk17 as gradleimage
COPY . /home/gradle/source
WORKDIR /home/gradle/source
RUN gradle build

FROM openjdk:17-alpine
COPY --from=gradleimage /home/gradle/source/build/libs/dj-navigator-backend-1.0.0.jar /app/
WORKDIR /app
ENTRYPOINT ["java", "-jar", "dj-navigator-backend-1.0.0.jar"]