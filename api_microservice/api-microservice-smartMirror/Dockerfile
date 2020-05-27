FROM openjdk:11.0-jre-slim-buster AS doctor-app
WORKDIR /usr/local/runme
ENV CN=esp30_smartMirrorDcApp
COPY target/api-microservice-smartMirror-0.0.1-SNAPSHOT.jar smartMirrorDcApp.jar
ENTRYPOINT ["java", "-jar", "smartMirrorDcApp.jar"]