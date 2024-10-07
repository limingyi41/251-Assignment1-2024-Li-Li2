FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/texteditor-1.0-SNAPSHOT.jar /app/texteditor.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/texteditor.jar"]
