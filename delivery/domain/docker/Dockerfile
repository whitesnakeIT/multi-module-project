FROM openjdk:latest

LABEL authors="tomasz"

#ENTRYPOINT ["top", "-b"]

RUN mkdir /usr/local/app

COPY . /usr/local/app

WORKDIR /usr/local/app

RUN mv delivery-delivery-domain-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
