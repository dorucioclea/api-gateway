Trust1Gateway: API Management Services based on open source KONG gateway
========================================================================
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/23b9a7b26ef44a2b886221fea10a70c7)](https://www.codacy.com?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Trust1Team/api-gateway&amp;utm_campaign=Badge_Grade)

[![][t1g-logo]][Trust1Gateway-url]

Summary: Open source API manager build on [Kong](https://getkong.org/)



Github project
--------------
Source: <https://github.com/Trust1Team/api-gateway>

Kong version
------------

![][kong-logo]

Using the open source Kong 0.10.1 (CE)

Documentation
-------------
We are working on the Trust1Gateway documentation:
[Trust1Gateway - Documentation](https://www.gitbook.com/book/t1t/trust1gateway-marketplace-guide/details)


Build
-----
In order to build the project, the ejb module contains the necessary profiles. Example maven build for production environment:
`mvn clean install`

Example build for dev:
`clean install`

You can customize the artifact name adding a 'targetenv' property for building:
`clean install -Dtargetenv=t1t`

You can customize artifact and define profile at the same time, for example:
`clean install -Dtargetenv=dev`

Build and prepare a docker container
`clean install -DskipTests=true -Pdocker`

If you want to build an API-Gateway with specific profile, but prepare it for a docker container, you can combine profiles:
`clean install -Pt1t-dev,docker`

Docker - Demo
-------------
We are still working on a docker container, so be patient :-)
TODO provision docker through repository
TODO docker compose for demo setup

Docker - Development environment
--------------------------------
In the target folder of t1g-distro you can find 2 artifacts:
- docker
- docker-postgres

Off course first you should provide a postgres container, a kong container, and after that run an api-gateway container linked to the postgres container.
### Prerequisites
Install docker on your machine

* [Mac OSX](https://docs.docker.com/engine/installation/mac/)
* [Windows](https://docs.docker.com/engine/installation/windows/)

### Create a new docker-machine (virtual image)
```sh
$ docker-machine create --driver virtualbox apiengine
```
### Login to your docker-machine
```sh
$ docker-machine ls
$ docker-machine env apiengine
$ eval $(docker-machine env apiengine)
```

### More information
More information, related to docker, can be found in the README.md file of the t1g-distro module.

### Pull a Cassandra container
```sh
$ docker pull cassandra:2.2.4
``` 

### Pull a Kong container
```sh
$ docker pull mashape/kong
```

### Start Cassandra
```sh
$ docker run -p 9042:9042 -d --name cassandra cassandra:2.2.4
```

### Start and link Kong to Cassandra
```sh
$ docker run -d --name kong \
            --link cassandra:cassandra \
            -p 8000:8000 \
            -p 8443:8443 \
            -p 8001:8001 \
            -p 7946:7946 \
            -p 7946:7946/udp \
            mashape/kong
```
```sh
$ docker run -d --name kong --link cassandra:cassandra -p 8000:8000 -p 8443:8443 -p 8001:8001 -p 7946:7946 -p 7946:7946/udp mashape/kong
```

### Build docker container
In the t1g-distro target folder, go to the docker-postgres folder and execute:
```sh
$ docker build -t api-db .
``` 

### See images
Verify the images has been created
```sh
$ docker images
```
Remark: You'll see an image for api-db and for postgres (where it depends on)

### Run docker container
Run the Postgres container:
```sh
$ docker run -p 5432:5432 --name api-gateway-db -t api-db
``` 
If you have already a container with this name you can easily remove it with:
```sh
$ docker rm api-gateway-db
```
or inspect more info
```sh
$ docker inspect api-gateway-db
```

### Verify running docker containers
```sh
$ docker ps
```

### Build Apiengine in target folder
In the t1g-distro target folder, go to the docker folder and execute:
```sh
$ docker build -t api-gateway .
```

### Run and connect API Engine to the running postgres instance
```sh
$ docker run -p 8080:8080 -p 9990:9990  --name api-gateway-inst1 --link api-gateway-db:postgres --link kong:kong -d api-gateway
```

### Verify your ip of the running machine
```sh
$ docker-machine ip apiengine
```
By default the management console is enabled.
Management console on IP:9990 username:admin password:admin123!
Web API on IP:8080/t1g-web
Auth API on IP:8080/t1g-auth

### remove a running container and its image
```sh
$ docker stop api-gateway-inst1
$ docker rm api-gateway-inst1
$ docker rmi api-gateway
```
Using the API Marketplace
-------------------------
Start your Kong container by adding your localhost IP:
```sh
$ docker run --add-host=localhost:84.198.85.191 -d --name konglocal --link cassandra:cassandra -p 8000:8000 -p 8443:8443 -p 8001:8001 -p 7946:7946 -p 7946:7946/udp mashape/kong 
```

See [Docker-info](https://docs.docker.com/engine/reference/commandline/run/#add-entries-to-container-hosts-file-add-host)



[Trust1Team-url]: https://trust1team.com
[Trust1Gateway-url]: https://www.trust1gateway.com
[Github-T1G]: https://github.com/Trust1Team/api-gateway 
[t1t-logo]: http://imgur.com/lukAaxx.png
[t1c-logo]: http://i.imgur.com/We0DIvj.png
[t1g-logo]: https://i.imgur.com/zsGZaoC.png
[t1g-documentation]: https://www.gitbook.com/book/t1t/trust1gateway-marketplace-guide/details
[kong-logo]: https://i.imgur.com/ykM19BJ.png
[kong-uri]: https://getkong.org/