# 使用 OpenJDK 17 作为基础镜像
FROM openjdk:17-jdk-slim

# 设置工作目录
WORKDIR /app

# 将构建好的 JAR 文件复制到容器中
COPY target/texteditor3-1.0-SNAPSHOT.jar /app/texteditor3.jar



# 设置容器启动时执行的命令
ENTRYPOINT ["java", "-jar", "/app/texteditor3.jar"]
