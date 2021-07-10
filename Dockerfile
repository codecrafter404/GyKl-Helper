ARG ARCH
# arm64v8/openjdk or openjdk
FROM ${ARCH}openjdk:11
WORKDIR /app
COPY ./target/VPlanBOT-1.0-jar-with-dependencies.jar ./VPlanBot.jar
RUN if [ "$ARCH" = "arm64v8/" ] ; then curl https://raw.githubusercontent.com/ollihoo/phantomjs-docker-rpi/master/phantomjs -o /usr/local/bin/phantomjs; fi
ENV VPLAN_PATH=${ARCH:+/usr/local/bin/phantomjs}
ENV OPENSSL_CONF=/opt/openssl.cnf
ENTRYPOINT ["java", "-jar", "./VPlanBot.jar"]
CMD ["exit"]