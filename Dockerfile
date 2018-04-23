# sudo docker system prune
# sudo docker build -t v.cherkov .
# sudo docker run -p 5000:5000 --name v.cherkov -t v.cherkov
# sudo docker rm v.cherkov

FROM ubuntu:16.04
LABEL maintainer="vv-ch@bk.ru"

RUN apt-get -y update
RUN apt-get install -y apt-utils
RUN apt-get install -y dialog

ENV PG_VERSION=9.5

RUN apt-get install -y postgresql-${PG_VERSION}

USER postgres

RUN /etc/init.d/postgresql start &&\
    psql --command "CREATE USER docker WITH SUPERUSER PASSWORD 'docker';" &&\
    createdb -O docker docker &&\
    /etc/init.d/postgresql stop

RUN echo "host all  all    0.0.0.0/0  md5" >> /etc/postgresql/${PG_VERSION}/main/pg_hba.conf

RUN echo "listen_addresses='*'" >> /etc/postgresql/${PG_VERSION}/main/postgresql.conf

RUN echo "synchronous_commit = off" >> /etc/postgresql/${PG_VERSION}/main/postgresql.conf

# Порт БД
EXPOSE 5432

VOLUME  ["/etc/postgresql", "/var/log/postgresql", "/var/lib/postgresql"]

USER root

RUN apt-get install -y openjdk-9-jdk-headless
RUN apt-get install -y maven

ENV WORK /opt/database-project
ADD spring-boot/ ${WORK}/spring-boot/

WORKDIR ${WORK}/spring-boot

RUN mvn package

# Порт приложения
EXPOSE 5000

CMD service postgresql start && java -Xmx300M -Xmx300M -jar ${WORK}/spring-boot/target/database-docker.jar