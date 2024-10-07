# 使用 OpenJDK 17 的官方镜像
FROM openjdk:17-jdk-slim

# 创建应用程序的工作目录
WORKDIR /app

# 将项目编译后的 JAR 文件复制到容器中
COPY target/texteditor3-1.0-SNAPSHOT.jar /app/texteditor3.jar

# 暴露应用的运行端口（如果需要，例如 8080）
EXPOSE 8080

# 运行 JAR 文件
ENTRYPOINT ["java", "-jar", "texteditor.jar"]
