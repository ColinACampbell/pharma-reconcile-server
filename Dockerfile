FROM openjdk:latest
CMD ./mvnw clean install -DskipTests
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/application.jar","--spring.profiles.active=prod"]