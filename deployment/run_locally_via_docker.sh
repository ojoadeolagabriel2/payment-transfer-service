#!/bin/sh

set -xe

# shellcheck disable=SC2039
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && cd ../ && pwd )"
SERVICE_NAME="${ENV_SERVICE_NAME:-payment-transfer-service}"

# script vars
IMAGE_NAME="bank.saudi.fransi/${SERVICE_NAME}"
VERSION="1.${2:-$(date +%Y%m%d%H%M%S)}"

# switch to project dir
cd "$DIR"

# package app
mvn clean package spring-boot:repackage

# build latest image
docker build -t "${IMAGE_NAME}:latest" .
# build versioned image
docker build -t "${IMAGE_NAME}:${VERSION}" .

mvn clean

# run image...
docker stop "${SERVICE_NAME}" && docker rm "${SERVICE_NAME}" >/dev/null 2>&1
docker run -d -p 40001:40000 --name "${SERVICE_NAME}" "${IMAGE_NAME}"
