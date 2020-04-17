#FROM payara/server-full
#COPY target/smartMirror-0.0.1.1.jar $DEPLOY_DIR

FROM openjdk:8-jre-alpine
COPY target/smartMirror-0.0.1.1.jar /usr/src/myapp
WORKDIR /usr/src/myapp
ENTRYPOINT ["java", "-jar", "smartMirror-0.0.1.1.jar"]