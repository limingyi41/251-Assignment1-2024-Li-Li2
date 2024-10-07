# 使用 OpenJDK 17 作为基础镜像
FROM openjdk:17-jdk-slim

# 设置工作目录
WORKDIR /app

# 将编译后的 jar 文件复制到容器中
COPY target/texteditor-1.0-SNAPSHOT.jar /app/texteditor.jar

# 暴露应用运行的端口（如应用使用8080端口）
EXPOSE 8080

# 运行 jar 文件
CMD ["java", "-jar", "/app/texteditor.jar"]
