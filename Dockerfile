#FROM payara/server-full
#COPY target/smartMirror-0.0.1.1.jar $DEPLOY_DIR

FROM openjdk:8-jre-slim-buster AS front-end
WORKDIR /usr/local/runme
ENV CN=esp30_smartMirrorFE
COPY target/smartMirror-0.0.1.1.jar smartMirror.jar
ENTRYPOINT ["java","-XX:+UseSerialGC", "-Xss512k", "-XX:MaxRAM=150m", "-jar", "smartMirror.jar"]

FROM python:3.6-slim-buster AS proc-unit
COPY python_src/emoteDetection.py app/emoteDetection.py
COPY python_src/requirements.txt  app/requirements.txt
COPY python_src/models/*          app/models/
WORKDIR app/
RUN apt-get update && apt-get -y upgrade
RUN apt-get -y install build-essential && apt-get -y install libglib2.0-0 && apt-get install -y libsm6 libxext6 libxrender-dev
RUN which python3
RUN python3 -m pip install --upgrade pip
RUN python3 -m pip install -r requirements.txt
RUN ls
ENTRYPOINT ["python3", "emoteDetection.py"]