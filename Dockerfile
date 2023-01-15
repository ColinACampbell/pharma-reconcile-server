FROM amazoncorretto:11
CMD ./mvnw clean install -DskipTests
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
EXPOSE 8080

ENTRYPOINT ["java","-jar","/application.jar","--spring.profiles.active=prod","--spring.datasource.driver-class-name=org.postgresql.Driver", "--spring.datasource.url=${DB_HOST}","--spring.datasource.password=${DB_PASSWORD}","--spring.datasource.username=${DB_USERNAME}"]