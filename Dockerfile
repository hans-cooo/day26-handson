FROM tomcat:11-jdk21

RUN rm -rf /usr/local/tomcat/webapps/*

COPY target/ecommerceapp-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ecommerceapp.war

EXPOSE 8080