# Build image
```bash
docker build -t trust1gateway:latest .
```

# Run container
```bash
docker run -dit -p 127.0.0.1:5433:5432 -p 127.0.0.1:3000:3000 -p 127.0.0.1:3003:3003 -p 127.0.0.1:8443:8443 -p 127.0.0.1:8000:8000 -p 127.0.0.1:8001:8001 -p 127.0.0.1:28080:28080 -p 127.0.0.1:28443:28443 -p 127.0.0.1:29990:29990 -p 127.0.0.1:29993:29993 --name t1g-docker trust1gateway:latest /tmp/start.sh
docker start t1g-docker
```

# Log in to container:
```bash
docker exec -u 0 -it t1g-docker /bin/bash
```

# URLs
[Keycloak](http://localhost:28080/auth)
[T1G Engine Web Swagger](http://localhost:28080/t1g-web)
[T1G Engine Auth Swagger](http://localhost:28080/t1g-auth)
[Wildfly](http://localhost:28080)
[Kong Gateway](http://localhost:8000)
[Kong Gateway Admin](http://localhost:8001)
PostgreSQL server available on port `5433`

# Credentials

## Keycloak

### Master Realm

username: t1gadmin
password: admin

### Trust1Gateway Realm

username: t1guser
password: user