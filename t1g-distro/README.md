### See all docker images
```sh
$ docker images
```

### Remove images
```sh
$ docker rmi image_id
```

### Delete all containers
```sh
$ docker rm $(docker ps -a -q)
```

### Delete all images
```sh
$ docker rmi $(docker images -q)
```

### Force remove all images (including links)
### Delete all images
```sh
$ docker rmi -f $(docker images -q)
```

### Build docker contianer
```sh
$ docker build -t api-db .
``` 

### See images
```sh
$ docker images
```
You'll see an image for api-db and for postgres (where it depends on)

### Run docker container
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

### See running docker containers
```sh
$ docker ps
```

### Build Apiengine in target folder
```sh
$ docker build -t api-gateway .
```

### Run and connect API Engine to the running postgres instance
```sh
$ docker run -p 8080:8080 -p 9990:9990  --name api-gateway-inst1 --link api-gateway-db:postgres -d api-gateway
```

### Verify your ip of the running machine
```sh
$ docker-machine ip apiengine
```

Management console on IP:9990 username:admin password:admin123!
API on IP:8080 

### remove a running container and its image
```sh
$ docker stop api-gateway-inst1
$ docker rm api-gateway-inst1
$ docker rmi api-gateway
```





