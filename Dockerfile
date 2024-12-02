FROM openjdk:17-jdk-alpine
COPY build/libs/short-url-app.jar urlshortapp.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "urlshortapp.jar"]
