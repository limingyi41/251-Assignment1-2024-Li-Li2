# 使用 OpenJDK 17 作为基础镜像
FROM openjdk:17-jdk-slim
LABEL author="MingYiLi"
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean install

# 将构建好的 JAR 文件复制到容器中
COPY --from=build /app/target/texteditor3-1.0-SNAPSHOT.jar /app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
ENTRYPOINT ["top","-b"]
