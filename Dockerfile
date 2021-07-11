FROM openjdk:11
WORKDIR /app
COPY ./target/VPlanBOT-1.0-jar-with-dependencies.jar ./VPlanBot.jar
RUN mkdir -p /tmp/arm/
RUN apt-get install -qy git curl build-essential g++ flex bison gperf ruby perl libsqlite3-dev libfontconfig1-dev libicu-dev libfreetype6 libssl-dev libpng-dev libjpeg-dev python libx11-dev libxext-dev python python-pip
RUN git clone git://github.com/ariya/phantomjs.git /tmp/source && cd /tmp/source && git checkout 2.1.1 && git submodule init && git submodule update && python build.py
RUN mv /tmp/source/bin/phantomjs /usr/local/bin

ENV OPENSSL_CONF=/opt/openssl.cnf
ENTRYPOINT ["java", "-jar", "./VPlanBot.jar"]
CMD ["exit"]