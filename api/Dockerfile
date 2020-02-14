FROM maven:3-jdk-8

RUN apt-get update

RUN apt-get install -y git

WORKDIR /api

COPY ./apiresource /api
COPY ./maven_repo /root/.m2/repository


CMD "./startup.sh"