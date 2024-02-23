FROM amazoncorretto:17-alpine-jdk

WORKDIR /backend
COPY . .
RUN ./gradlew clean build

EXPOSE 8080

CMD ["java", "-jar", "./build/libs/trading-platform-0.0.1-SNAPSHOT.jar"]
