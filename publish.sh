# docker build -t form-team-talent:latest .

docker network create form-team-talent-network

docker run --name mysqldb \
-p 3306:3306 \
--network form-team-talent-network \
-v /docker/mysql/conf:/etc/mysql/conf.d \
-v /docker/mysql/logs:/logs \
-v /docker/mysql/data:/var/lib/mysql \
-e MYSQL_ROOT_PASSWORD=658766@Jzh \
-d mysql:5.7

docker run --name form-team-talent \
-p 8080:8080 \
--network form-team-talent-network \
-d form-team-talent:latest