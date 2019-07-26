#!/bin/sh

REPO='aterefe/http4s'
LOCATION=k8s/plain

IMAGE=$1
if [ -z "$IMAGE" ]; then
  IMAGE="$REPO:latest"
fi

sed -i.bak -e "s|$REPO:.*|$IMAGE|g" -- ./${LOCATION}/deployment.yml;


cat ${LOCATION}/deployment.yml;

kubectl apply -f ${LOCATION}/namespace.yml
kubectl apply -f ${LOCATION}/deployment.yml
kubectl apply -f ${LOCATION}/service.yml
