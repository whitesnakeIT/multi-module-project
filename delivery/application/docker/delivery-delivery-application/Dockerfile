FROM openjdk:latest

LABEL authors="tomasz"

RUN mkdir /usr/local/app

COPY target/delivery-delivery-application-0.0.1-SNAPSHOT.jar /usr/local/app

WORKDIR /usr/local/app

RUN mv delivery-delivery-application-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
