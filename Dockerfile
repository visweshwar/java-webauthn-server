FROM tomcat:8.0

#Copy application JAR
ARG WAR_FILE=webauthn-server-demo/build/libs/*.war
COPY ${WAR_FILE} /usr/local/tomcat/webapps/
ENV YUBICO_WEBAUTHN_ALLOWED_ORIGINS "https://yubico.us-east.payx-demo.com"
EXPOSE 8443
#COPY script.sh script.sh
#RUN chmod +x script.sh
#
#CMD ["/bin/sh","script.sh"]
