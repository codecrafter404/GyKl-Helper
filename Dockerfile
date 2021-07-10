FROM openjdk:11
WORKDIR /app
COPY ./target/VPlanBOT-1.0-jar-with-dependencies.jar ./VPlanBot.jar
RUN mkdir -p /tmp/arm/
RUN curl https://raw.githubusercontent.com/piksel/phantomjs-raspberrypi/master/bin/phantomjs -o /tmp/arm/phantomjs
RUN chmod 777 /tmp/arm/phantomjs
# Install common build dependencies
RUN apt-get update && apt-get install -y --no-install-recommends \
    ca-certificates \
    curl \
    bzr \
    mercurial \
    subversion \
    procps \
    autoconf \
    automake \
    bzip2 \
    file \
    g++ \
    gcc \
    imagemagick \
    libbz2-dev \
    libc6-dev \
    libcurl4-openssl-dev \
    libevent-dev \
    libffi-dev \
    libgeoip-dev \
    libglib2.0-dev \
    libjpeg-dev \
    liblzma-dev \
    libmagickcore-dev \
    libmagickwand-dev \
    libmysqlclient-dev \
    libncurses-dev \
    libpng-dev \
    libpq-dev \
    libreadline-dev \
    libsqlite3-dev \
    libssl-dev \
    libtool \
    libwebp-dev \
    libxml2-dev \
    libxslt-dev \
    libyaml-dev \
    make \
    patch \
    xz-utils \
    zlib1g-dev \
  && rm -rf /var/lib/apt/lists/* \
  && apt-get clean
ENV OPENSSL_CONF=/opt/openssl.cnf
ENTRYPOINT ["java", "-jar", "./VPlanBot.jar"]
CMD ["exit"]