#!/bin/sh

REPO='aterefe/http4s'
LOCATION=./k8s/mortar

# Usage
# Typically from jenkins
# ./mortar-run.sh [IMAGE:TAG]
# ./mortar-run.sh aterefe/http4s:HEAD-22e144d

IMAGE=$1
if [ -z "$IMAGE" ]; then
  IMAGE="$REPO:latest"
fi

sed -i.bak -e "s|$REPO:.*|$IMAGE|g" -- ./${LOCATION}/shot.yml;

cat ${LOCATION}/shot.yml;

cd $LOCATION
mortar fire . hello-world
