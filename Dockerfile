FROM ascdc/jdk8 as jdk

# 设置工作目录为jar包所在目录，运行时可以读取工作目录下的外部配置文件application.yaml
WORKDIR /docker/app/form-team-talent

ENTRYPOINT ["java", "-jar", "app.jar"]