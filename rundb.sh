#!/bin/sh
sudo /etc/init.d/postgresql start
sudo -u postgres dropdb docker
sudo -u postgres psql --command "DROP ROLE docker;" 
sudo -u postgres psql --command "CREATE USER docker WITH PASSWORD 'docker';" 
sudo -u postgres psql --command "ALTER USER docker WITH SUPERUSER;"
sudo -u postgres createdb -T template0 -E UTF8 -O docker docker
echo "------------ [OK] Ready ------------"
