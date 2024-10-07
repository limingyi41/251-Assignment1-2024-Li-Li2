# 使用 OpenJDK 基础镜像
FROM openjdk:17-jdk-slim

# 设置工作目录
WORKDIR /app

# 将编译后的 JAR 文件复制到容器中
COPY target/texteditor-1.0-SNAPSHOT.jar /app/texteditor.jar

# 设置容器启动时执行的命令
CMD ["java", "-jar", "/app/texteditor.jar"]

# 暴露应用运行的端口（例如8080）
EXPOSE 8080
