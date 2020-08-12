# Container image that runs your code
FROM gradle:4.5.0-jdk8-alpine

# Copies your code file from your action repository to the filesystem path `/` of the container
COPY entrypoint.sh /entrypoint.sh

# Code file to execute when the docker container starts up (`entrypoint.sh`)
CMD sh /entrypoint.sh
