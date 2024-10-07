# 使用 OpenJDK 17 作为基础镜像
FROM openjdk:17-jdk-slim

# 创建一个工作目录
WORKDIR /app

# 将构建好的 JAR 文件复制到容器中
COPY target/texteditor-1.0-SNAPSHOT.jar /app/texteditor.jar

# 暴露应用使用的端口（如果需要）
# EXPOSE 8080

# 设置容器启动时执行的命令
CMD ["java", "-jar", "/app/texteditor.jar"]
