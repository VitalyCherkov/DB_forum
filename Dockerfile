
# sudo docker system prune
# sudo docker build -t v.cherkov .
# sudo docker run -p 5000:5000 --name v.cherkov -t v.cherkov
# docker stop v.cherkov && docker rm v.cherkov 

FROM ubuntu:16.04
LABEL maintainer="vv-ch@bk.ru"

ENV PG_VERSION=9.5

RUN apt-get -y update && \
    apt-get -y upgrade && \
    apt-get install -y dialog &&\
    apt-get install -y apt-utils &&\
    apt-get install -y build-essential && \
    apt-get install -y software-properties-common && \
    apt-get install -y byobu curl git htop man unzip vim wget && \
    apt-get install -y postgresql-${PG_VERSION} &&\
    apt-get install -y openjdk-9-jdk-headless &&\
    apt-get install -y maven &&\
    apt-get clean

###########################################################################################

USER postgres

RUN /etc/init.d/postgresql start &&\
    psql --command "CREATE USER docker WITH SUPERUSER PASSWORD 'docker';" &&\
    createdb -T template0 -E UTF8 -O docker docker &&\
    /etc/init.d/postgresql stop

RUN echo "host all  all    0.0.0.0/0  md5" >> /etc/postgresql/${PG_VERSION}/main/pg_hba.conf

RUN echo "listen_addresses='*'" >> /etc/postgresql/${PG_VERSION}/main/postgresql.conf

RUN echo "synchronous_commit = off" >> /etc/postgresql/${PG_VERSION}/main/postgresql.conf

# Порт БД
EXPOSE 5432

VOLUME  ["/etc/postgresql", "/var/log/postgresql", "/var/lib/postgresql"]

USER root

###########################################################################################

# ENV JAVA_HOME /usr/lib/jvm/java-9-oracle

ENV WORK /opt/database-project
ADD spring-boot/ ${WORK}/spring-boot/

WORKDIR ${WORK}/spring-boot

RUN mvn package

# Порт приложения
EXPOSE 5000

CMD service postgresql start &&\
    java -Xmx300M -Xmx300M -jar ${WORK}/spring-boot/target/database-docker.jar