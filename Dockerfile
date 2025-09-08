FROM eclipse-temurin:8-jdk

WORKDIR /app

COPY target/tinyurl-demo.jar app.jar

ENTRYPOINT ["java","-jar","app.jar","--spring.profiles.active=${SPRING_PROFILE:dev}"]

