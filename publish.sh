if [ "$1" = "-u" ] # 更新jar包
then
  mvn clean && mvn package
  cp -a ./target/*.jar /docker/app/form-team-talent/app.jar
fi

docker-compose up --build -d