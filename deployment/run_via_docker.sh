#!/usr/bin/env sh

set -ex

if [ -z "$SERVICE_NAME" ]; then
  echo "aborting initiating of service as env service name is empty. please check"
elif [ -z "$IMAGE_NAME" ]; then
  echo "aborting initiating of service as env image name is empty. please check"
else
  echo "initiating image: $IMAGE_NAME"
  docker stop "${SERVICE_NAME}" && docker rm "${SERVICE_NAME}" >/dev/null 2>&1
  docker run -d -p 40001:40000 --name "${SERVICE_NAME}" "${IMAGE_NAME}"
  echo "completed initiation of image: $IMAGE_NAME"
fi