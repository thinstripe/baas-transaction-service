#!/bin/bash

service="transaction-service"

image_repository="gcr.io/endless-codex-286306/$service"
image_tag="latest"
image="$image_repository:$image_tag"

eval $(minikube -p minikube docker-env)

./gradlew bootJar && \
docker build -t $image . && \
docker push $image

kubectl create secret generic client-token --from-literal clientid=test --from-literal secret=test --from-literal dbpassword=test

pushd helm
helm upgrade --install -f values.yaml \
	--set cbs.provider=tm \
	--set config.core_api_base=https://core-api-demo.singapore-orchid.tmachine.io \
	--set config.sa_token=A0009193302352369176633!WYDKfsssZJPgkDoXrTD1h7+vGuslLwzm91M5yoqHbWqLX1hh5PPXcduP4WNKEnLXigBfX57dPHssN1n5kyKdgqVZh2U= \
   tm-$service .
helm upgrade --install -f values.yaml \
	--set cbs.provider=ow \
   ow-$service .
popd

for cbs in tm ow; do
  kubectl rollout restart deployment/$cbs-$service
done
