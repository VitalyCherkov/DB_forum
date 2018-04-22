FROM ubuntu:16.04
LABEL maintainer="vv-ch@bk.ru"

RUN apt-get -y update

ENV PG_VERSION=9.5

RUN apt-get install -y postgresql-${PG_VERSION}

USER postgres

RUN /etc/init.d/postgresql start &&\
    psql --command "CREATE USER docker WITH SUPERUSER PASSWORD 'docker';" &&\
    createdb -O docker docker &&\
    /etc/init.d/postgresql stop

RUN echo "host all  all    0.0.0.0/0  md5" >> /etc/postgresql/$PGVER/main/pg_hba.conf

RUN echo "listen_addresses='*'" >> /etc/postgresql/$PGVER/main/postgresql.conf

RUN echo "synchronous_commit = off" >> /etc/postgresql/$PGVER/main/postgresql.conf

# Порт БД
EXPOSE 5050

VOLUME  ["/etc/postgresql", "/var/log/postgresql", "/var/lib/postgresql"]

USER root

RUN apt-get -y install software-properties-common python-software-properties
RUN add-apt-repository ppa:webupd8team/java
RUN apt-get -y update
RUN echo oracle-java9-installer shared/accepted-oracle-license-v1-1 select true | debconf-set-selections && apt-get -y install oracle-java9-installer && apt-get -y install oracle-java9-set-default

RUN java -version
RUN javac -version

RUN apt-get install -y maven

ENV WORK /opt/database-project
ADD spring-boot/ ${WORK}/spring-boot/

WORKDIR ${WORK}/spring-boot

RUN mvn package

# Порт приложения
EXPOSE 5000

CMD service postgresql start && java -Xmx300M -Xmx300M -jar ${WORK}/spring-boot/target/database-docker.jar