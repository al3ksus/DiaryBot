FROM maven:3.8.6-openjdk-18 AS build
WORKDIR /app
COPY src/main /app/src/main
COPY pom.xml .
RUN  mvn -f pom.xml
RUN ls target

FROM openjdk:18-jdk as runner
RUN ls
COPY --from=build /app/target/DiaryBot-0.0.1-SNAPSHOT.jar .
EXPOSE 8000
ENTRYPOINT ["java","-jar","./DiaryBot-0.0.1-SNAPSHOT.jar"]