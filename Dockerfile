FROM openjdk:17
EXPOSE 8080
ADD web-chat.jar web-chat.jar
COPY config/log-back.xml log-back.xml
ENTRYPOINT ["java","-jar","/web-chat.jar"]
