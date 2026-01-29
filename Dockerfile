FROM eclipse-temurin:21-jre-alpine

WORKDIR /usr/share/app

COPY target/customer-service-*.jar app.jar

EXPOSE 8080

CMD [ "java", "-jar", "app.jar" ]