kubectl create secret generic regcred \
    --from-file=.dockerconfigjson=./private/.docker/config.json \
    --type=kubernetes.io/dockerconfigjson \
    -n http4s

