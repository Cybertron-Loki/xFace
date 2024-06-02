FROM java:8

WORKDIR /app

COPY target/xFace-0.0.1-SNAPSHOT.jar /app/xFace-0.0.1-SNAPSHOT.jar

EXPOSE 8089

# TODO 待修改  找不到主类 maven 打包异常待处理
CMD ["java","-jar","xFace-0.0.1-SNAPSHOT.jar","--spring.profiles.active=dev"]