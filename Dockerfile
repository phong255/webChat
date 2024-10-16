FROM openjdk:17
WORKDIR /opt/web-chat/
EXPOSE 8080
ADD web-chat.jar web-chat.jar
COPY config/log-back.xml log-back.xml
COPY /opt/web-chat/files/ files/
ENTRYPOINT ["java","-jar","/web-chat.jar"]
