#!/bin/sh

# sudo echo "host all  all    0.0.0.0/0  md5" >> /etc/postgresql/9.5/main/pg_hba.conf

sudo /etc/init.d/postgresql start
sudo -i -u postgres &&\
dropdb docker &&\
psql --command "DROP ROLE docker;" 
psql --command "CREATE USER docker WITH PASSWORD 'docker';" 
psql --command "ALTER USER docker WITH SUPERUSER;"
psql --command "CREATE DATABASE docker OWNER docker"
createdb -T template0 -E UTF8 -O docker docker
psql -U docker -h 127.0.0.1 -d docker -a -f /home/vitaly/projects/DB_forum/testDB.sql
# /etc/init.d/postgresql stop

# echo "listen_addresses='*'" >> /etc/postgresql/9.5/main/postgresql.conf
# echo "synchronous_commit = off" >> /etc/postgresql/9.5/main/postgresql.conf