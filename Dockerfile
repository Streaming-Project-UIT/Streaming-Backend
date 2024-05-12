# FROM openjdk:17-jdk

# WORKDIR /app

# COPY target/streaming-0.0.1-SNAPSHOT.jar /app.jar
# EXPOSE 8080


# CMD [ "java", "-jar", "/app.jar" ]


FROM ubuntu:latest

# Cài đặt các công cụ mạng
RUN apt-get update && \
    apt-get install -y iproute2 iputils-ping netcat && \
    rm -rf /var/lib/apt/lists/*

# Thiết lập thư mục làm việc
WORKDIR /app

# Sao chép file jar vào thư mục làm việc trong container
COPY target/streaming-0.0.1-SNAPSHOT.jar /app.jar

# Mở cổng 8080 để giao tiếp với container bên ngoài
EXPOSE 8080

# Lệnh chạy ứng dụng Java
CMD [ "java", "-jar", "/app.jar" ]

