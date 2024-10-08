# 使用 Maven 构建阶段
FROM maven:3.8.3-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean install

# 运行时阶段
FROM openjdk:17-jdk-slim
WORKDIR /app

# 从构建阶段复制 JAR 文件
COPY --from=build /app/target/texteditor3-1.0-SNAPSHOT.jar /app.jar

# 启动应用
ENTRYPOINT ["java", "-jar", "/app.jar"]
