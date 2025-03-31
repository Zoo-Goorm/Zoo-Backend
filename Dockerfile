FROM openjdk:17-jdk-slim

WORKDIR /build

COPY build/libs/InsightNote-0.0.1-SNAPSHOT.jar /build/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Dserver.env=${ENV}" ,"-jar", "app.jar"]