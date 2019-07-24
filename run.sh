#!/bin/sh

REPO='aterefe/ordering-system'
IMAGE=$1
if [ -z "$IMAGE" ]; then
  IMAGE="$REPO:latest"
fi

sed -i.bak -e "s|$REPO:.*|$IMAGE|g" -- ./k8s/deployment.yml;

cat k8s/deployment.yml;

# kubectl apply -f k8s/namespace.yml
# kubectl apply -f k8s/deployment.yml
# kubectl apply -f k8s/service.yml
