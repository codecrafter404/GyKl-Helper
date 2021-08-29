FROM openjdk:11
WORKDIR /app
COPY ./target/GyKl_Helper-1.0-jar-with-dependencies.jar ./GyKl_Helper.jar
RUN mkdir -p /tmp/arm/
COPY ./arm/phantomjs /tmp/arm/phantomjs
RUN chmod 777 /tmp/arm/phantomjs
ENV OPENSSL_CONF=/opt/openssl.cnf
ENTRYPOINT ["java", "-jar", "./GyKl_Helper.jar"]
CMD ["exit"]