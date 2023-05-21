FROM tomcat:latest

LABEL authors="tomasz"

COPY target/delivery.war /usr/local/tomcat/webapps/ROOT.war

WORKDIR /usr/local/tomcat

EXPOSE 8080

CMD ["bin/catalina.sh", "run"]
