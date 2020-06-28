FROM tomcat:8.0.51-jre8-alpine

#Copy application JAR
ARG WAR_FILE=webauthn-server-demo/build/libs/*.war
RUN rm -rf /usr/local/tomcat/webapps/*
COPY ${WAR_FILE} /usr/local/tomcat/webapps/app.war
ENV YUBICO_WEBAUTHN_ALLOWED_ORIGINS "https://yubico.us-east.payx-demo.com,payx-demo.com,http://yubico.us-east.payx-demo.com/app"
ENV YUBICO_WEBAUTHN_RP_ID "yubico.us-east.payx-demo.com"
EXPOSE 8080
#COPY script.sh script.sh
#RUN chmod +x script.sh
#
#CMD ["/bin/sh","script.sh"]
CMD ["catalina.sh","run"]
