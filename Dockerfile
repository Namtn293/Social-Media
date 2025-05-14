#use jdk 17
FROM maven:3.8.5-openjdk-17-slim AS build

WORKDIR /app

COPY target/media-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]

#delete old name:
#docker rm -f media-container

#run container
#docker run -p 8080:8080 --name media-container media-app