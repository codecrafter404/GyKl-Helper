FROM openjdk:11
WORKDIR /app
COPY ./target/VPlanBOT-1.0-jar-with-dependencies.jar ./VPlanBot.jar
RUN mkdir -p /tmp/arm/
RUN curl https://raw.githubusercontent.com/piksel/phantomjs-raspberrypi/master/bin/phantomjs -o /tmp/arm/phantomjs
RUN chmod 777 /tmp/arm/phantomjs
RUN apt-get update && apt-get install libc6 -y
ENV OPENSSL_CONF=/opt/openssl.cnf
ENTRYPOINT ["java", "-jar", "./VPlanBot.jar"]
CMD ["exit"]