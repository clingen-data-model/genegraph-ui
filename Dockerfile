# This is the dockerfile for executing yarn commands in the google cloudbuild,
# prior to running the firebase deploy
FROM ubuntu:20.04

ARG DEBIAN_FRONTEND=noninteractive
RUN apt-get update && apt-get install -y \
  npm \
  clojure \
  && rm -rf /var/lib/apt/lists/*

RUN npm install -g yarn
