FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/texteditor3-1.0-SNAPSHOT.jar /app/texteditor3.jar
CMD ["java", "-jar", "/app/texteditor3.jar"]
EXPOSE 80


