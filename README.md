Trust1Gateway: API Management Services based on open source KONG gateway
========================================================================
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/23b9a7b26ef44a2b886221fea10a70c7)](https://www.codacy.com?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Trust1Team/api-gateway&amp;utm_campaign=Badge_Grade)

[![][t1g-logo]][Trust1Gateway-url]

Summary: Open source API manager build on [Kong](https://getkong.org/) and [Keycloak](http://www.keycloak.org/)

Trust1Gateway has the following components:
- API Engine - management layer on top of Kong CE
- API Marketplace - open source front-end as developer portal
- API Publisher - open source front-end as portal to register new API's


Github project
--------------
- [API Engine source](https://github.com/Trust1Team/api-gateway)
- [API Marketplace source](https://github.com/Trust1Team/api-market)
- [API Publisher source](https://github.com/Trust1Team/api-publisher)

Kong version
------------

![][kong-logo]

Using the open source Kong 0.10.1 (CE)

Documentation
-------------
The Trust1Gateway product is an open source API Gateway built from the ground as a light-weight API Gateway with ease of use from a developer perspective, fast, scalable and resilient. The Trust1Gateway contains a developer portal, API management dashboard and much more. The Trust1Gateway provides a REST API on a business level in order to expose and consume API's fast and easy.

The Trust1Gateway uses components like Kong API Gateway, as the operational gateway build on NGINX; and Keycloak as the identity broker. The reason why Trust1Gateway integrated those components is to differentiate between:
- API gateway functionality
- IAM or IDP functionality

Both the API Gateway and IDP are orchestrated through the API Engine. The Trust1Gateway provides an administration UI on KONG CE. 
A full REST API is exposed to automate your product/service lifecycle completely or use the open source developer portals (marketplace and publisher). 
The API Engine provides a higher level of services registered:

- API versioning mechanism (deprecation, retirement, ..)
- API management for multiple organisations
- API branding
- API information, notification and light-weight issues management
- Marketplace management (multiple marketplaces can be provided depending on your needs)
- Policy management and configuration on consumer, service and plan level
- API monetization
- Private and public API's

The Trust1Gateway is oper source, and community ideas and support are very welcome. 

Marketplace documentation: [Trust1Gateway - Documentation](https://www.gitbook.com/book/t1t/trust1gateway-marketplace-guide/details)
Publisher documentation: [Trust1Gateway - Documentation](https://www.gitbook.com/book/t1t/trust1gateway-marketplace-guide/details)


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

### Prerequisites
Install docker on your machine

* [Mac OSX](https://docs.docker.com/engine/installation/mac/)
* [Windows](https://docs.docker.com/engine/installation/windows/)

### Pull the image
```sh
docker pull trust1team/trust1gateway-with-ui
```

### Run the container image
```sh
docker run -dit -p 127.0.0.1:5433:5432 -p 127.0.0.1:8443:8443 -p 127.0.0.1:8000:8000 -p 127.0.0.1:8001:8001 -p 127.0.0.1:28080:28080 -p 127.0.0.1:28443:28443 -p 127.0.0.1:29990:29990 -p 127.0.0.1:29993:29993 -p 127.0.0.1:3000:3000 -p 127.0.0.1:3003:3003 --name t1g-ui trust1team/trust1gateway-with-ui
```

If you have already a container with this name you can easily remove it with:
```sh
$ docker rm t1g-ui
```
or inspect more info
```sh
$ docker inspect t1g-ui
```

### Login to your docker container
```sh
docker exec -u 0 -it t1g-ui /bin/bash
```

#### Container URLs

* Keycloak: http://localhost:28080/auth
* T1G Engine Web Swagger: http://localhost:28080/t1g-web
* T1G Engine Auth Swagger: http://localhost:28080/t1g-auth
* T1G Publisher: http://localhost:3003
* T1G Marketplace: http://localhost:3000
* Wildfly: http://localhost:28080
* Kong Gateway: http://localhost:8000
* Kong Gateway Admin: http://localhost:8001
* PostgreSQL server available on port 5433

### More information
More information, related to docker, can be found in the README.md file of the t1g-distro module.

### Build docker container
In the t1g-distro target folder, go to the docker_t1g folder and execute:
```sh
docker build -t trust1team/trust1gateway-with-ui:latest .
``` 

### See images
Verify the images has been created
```sh
$ docker images
```

### Verify running docker containers
```sh
$ docker ps
```

### remove a running container and its image
```sh
$ docker stop t1g-ui
$ docker rm t1g-ui
$ docker rmi trust1team/trust1gateway-with-yu
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