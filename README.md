# Prototype for a Microservice ERP System using event sourcing

This repository contains an ERP Prototype built as a microservice architecture. The services implement the CQRS Pattern using event sourcing.

## How to run

Run the services using Docker.

### Compose
Run `docker-compose up` with or without `-d` and wait until all services are running. Stop the application by running `docker-compose down`.

### Swarm
To start a scaled swarm first initialize a swarm with `docker swarm init`. Then deploy the stack defined in the second compose file running `docker stack deploy -c docker-compose-img.yml <STACK NAME>`. Use any name you want for the stack. To stop the application remove the stack using `docker stack rm <STACK NAME>` and leave the swarm with `docker swarm leave -f`.

