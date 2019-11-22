FROM gradle:5.6.4-jdk11 AS build

COPY . /home/gradle/src
WORKDIR /home/gradle/src

RUN gradle clean build -q --no-daemon

FROM openjdk:11-jre-slim

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/*.jar /app/fibonacci-server.jar

EXPOSE 8080
EXPOSE 8081

CMD java -jar /app/fibonacci-server.jar