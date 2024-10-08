# 第一阶段：构建项目
FROM maven:3.8.3-openjdk-17 AS build
WORKDIR /app

# 复制 Maven 配置文件并下载依赖
COPY pom.xml .
RUN mvn dependency:go-offline

# 复制项目源文件并构建
COPY src ./src
RUN mvn clean install

# 第二阶段：运行项目
FROM openjdk:17-jdk-slim
WORKDIR /app

# 复制构建生成的 JAR 文件到运行环境
COPY --from=build /app/target/texteditor3-1.0-SNAPSHOT.jar /app.jar

# 设置容器的启动命令
CMD ["java", "-jar", "/app.jar"]
