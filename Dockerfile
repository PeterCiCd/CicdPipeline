FROM maven:3.8.6-jdk-11-slim as BUILD
COPY . /src
WORKDIR /src
RUN mvn install -DskipTests

FROM openjdk:11-jdk-slim
EXPOSE 8999
WORKDIR /app
ARG JAR=cicd-demo-app-1.0.0-SNAPSHOT.jar

COPY --from=BUILD /src/target/$JAR /opt/app.jar
ENTRYPOINT ["java","-jar","/opt/app.jar"]
