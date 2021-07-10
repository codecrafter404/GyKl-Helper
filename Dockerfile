FROM openjdk:11
WORKDIR /app
COPY ./target/VPlanBOT-1.0-jar-with-dependencies.jar ./VPlanBot.jar
RUN mkdir -p /tmp/arm/
RUN curl https://github.com/piksel/phantomjs-raspberrypi/raw/master/bin/phantomjs -o /tmp/arm/phantomjs
RUN chmod 777 /tmp/arm/phantomjs
ENV OPENSSL_CONF=/opt/openssl.cnf
ENTRYPOINT ["java", "-jar", "./VPlanBot.jar"]
CMD ["exit"]