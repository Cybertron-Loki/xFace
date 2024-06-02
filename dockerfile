FROM java:8

WORKDIR /app

COPY target/xFace-0.0.1-SNAPSHOT.jar /app/xFace-0.0.1-SNAPSHOT.jar

EXPOSE 8089

CMD ["java","-jar","xFace-0.0.1-SNAPSHOT.jar"]