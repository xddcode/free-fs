#构建镜像在DockerFile同级目录下执行命令： docker build -t free-fs .
FROM openjdk:17-jdk-alpine
ADD fs-admin/target/*.jar /app.jar
# 设置默认时区
ENV TZ=Asia/Shanghai
EXPOSE 8081
ENTRYPOINT ["java","-jar","/app.jar"]