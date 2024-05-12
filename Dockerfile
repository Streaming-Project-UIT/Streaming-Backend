# FROM openjdk:17-jdk

# WORKDIR /app

# COPY target/streaming-0.0.1-SNAPSHOT.jar /app.jar
# EXPOSE 8080


# CMD [ "java", "-jar", "/app.jar" ]


FROM openjdk:17-jdk

# Cài đặt các công cụ mạng
RUN apt-get update && \
    apt-get install -y iproute2 iputils-ping netcat

WORKDIR /app

COPY target/streaming-0.0.1-SNAPSHOT.jar /app.jar
EXPOSE 8080

CMD [ "java", "-jar", "/app.jar" ]
