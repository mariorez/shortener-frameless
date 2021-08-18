# Docker image

docker build . -t shortener-frameless
docker run -p 50051:50051 --network="host" -memory=1g --cpus="1.5" shortener-frameless

