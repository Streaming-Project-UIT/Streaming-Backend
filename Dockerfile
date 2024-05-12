FROM openjdk:17-jdk

RUN apt-get update && apt-get -y install net-tools
WORKDIR /app

COPY target/streaming-0.0.1-SNAPSHOT.jar /app.jar
EXPOSE 8080


CMD [ "java", "-jar", "/app.jar" ]