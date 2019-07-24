
REPO='aterefe/ordering-system'
IMAGE=$1
if [ -z "$TAG" ]; then
  IMAGE="$REPO:latest"
fi

sed -i -e 's/\$REPO:.*/\$IMAGE' k9s/deployment.yml;

kubectl apply -f k8s/namespace.yml
kubectl apply -f k8s/deployment.yml
kubectl apply -f k8s/service.yml
