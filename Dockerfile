FROM openjdk:17
EXPOSE 8080
ADD target/web-chat.jar web-chat.jar
COPY target/log-back.xml log-back.xml
ENTRYPOINT ["java","-jar","/web-chat.jar"]
