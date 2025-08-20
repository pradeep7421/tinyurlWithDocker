FROM eclipse-temurin:8-jdk
ENV MONGO_DB_USERNAME=root
ENV	MONGO_DB_PWD=root
RUN mkdir -p tinyurlWithDocker
COPY target/wc-java-tinyurl-service-docker-0.0.1-SNAPSHOT.jar app.jar

CMD ["java", "-jar", "app.jar"]
