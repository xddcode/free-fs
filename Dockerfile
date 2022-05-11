FROM openjdk:8-jdk-alpine
LABEL version="1.2.0" description="free-fs" by="dinghao"
#添加字体库，解决验证码报错
RUN apk add --no-cache ttf-dejavu
WORKDIR /home/free-fs
ADD target/free-fs.jar /home/free-fs
CMD ["java","-jar","free-fs.jar"]

