# syntax=docker/dockerfile:1
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

# copie o jar gerado (ajuste o nome se preciso)
ARG JAR_FILE=target/*SNAPSHOT*.jar
COPY ${JAR_FILE} app.jar

ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75 -Duser.timezone=America/Sao_Paulo"

# porta do MS A
EXPOSE 8081
ENTRYPOINT ["java","-jar","/app/app.jar"]
