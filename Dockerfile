FROM ascdc/jdk8 as jdk

MAINTAINER akynazh

WORKDIR /app/form-team-talent

COPY "./target/form-team-talent.jar" "app.jar"

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]