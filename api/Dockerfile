FROM maven:3-jdk-8

RUN apt-get update

RUN apt-get install -y git

WORKDIR /api

COPY ./apiresource /api


CMD "./startup.sh"
