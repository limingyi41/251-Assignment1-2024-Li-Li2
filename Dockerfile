FROM openjdk:17-jdk-slim
WORKDIR /app

CMD ["java", "-jar", "/app/texteditor3.jar"]
EXPOSE 80


