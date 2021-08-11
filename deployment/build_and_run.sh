#!/bin/sh

set -xe

# shellcheck disable=SC2039
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && cd ../ && pwd )"

# script vars
export ORG_PREFIX="bank.saudi.fransi"
export SERVICE_NAME="${ENV_SERVICE_NAME:-payment-transfer-service}"
export IMAGE_NAME="$ORG_PREFIX/$SERVICE_NAME"
export VERSION="1.${2:-$(date +%Y%m%d%H%M%S)}"

# switch to project dir
cd "$DIR"

# package app
mvn clean package spring-boot:repackage

# build latest image
docker build -t "${IMAGE_NAME}:latest" .
# build versioned image
docker build -t "${IMAGE_NAME}:${VERSION}" .
# cleanup
mvn clean

"$DIR"/deployment/run_via_docker.sh