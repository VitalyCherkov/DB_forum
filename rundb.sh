#!/bin/sh

# sudo echo "host all  all    0.0.0.0/0  md5" >> /etc/postgresql/9.5/main/pg_hba.conf

sudo /etc/init.d/postgresql start
dropdb docker
psql --command "DROP ROLE docker;"
psql --command "CREATE USER docker WITH PASSWORD 'docker';"
createdb docker
psql -U docker -d docker -a -f ./testDB.sql
# /etc/init.d/postgresql stop

# echo "listen_addresses='*'" >> /etc/postgresql/9.5/main/postgresql.conf
# echo "synchronous_commit = off" >> /etc/postgresql/9.5/main/postgresql.conf