FROM maven:3.5.2-jdk-8-alpine AS build
COPY . /tmp/
WORKDIR /tmp/
RUN mvn clean package
FROM payara/server-full
COPY --from=0 /tmp/target/smartMirror-0.0.1.1.war $DEPLOY_DIR