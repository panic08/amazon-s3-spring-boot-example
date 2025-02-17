FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . .

RUN chmod +x gradlew
RUN ./gradlew build

EXPOSE 9000

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "build/libs/amazon-s3-spring-boot-example-0.0.1-SNAPSHOT.jar"]