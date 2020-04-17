#FROM payara/server-full
#COPY target/smartMirror-0.0.1.1.jar $DEPLOY_DIR

FROM openjdk:8-jre-alpine
WORKDIR /usr/local/runme
COPY target/smartMirror-0.0.1.1.jar smartMirror.jar
ENTRYPOINT ["java", "-jar", "smartMirror.jar"]
