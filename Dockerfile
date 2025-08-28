FROM eclipse-temurin:8-jdk

RUN mkdir -p tinyurlWithDocker
COPY target/tinyurl-demo.jar app.jar

CMD ["java", "-jar", "app.jar"]
