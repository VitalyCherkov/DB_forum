#!/bin/sh
# Скрипт для Константина
echo "------------- [  ] Postgres установка  -------------"

sudo sh -c 'echo "deb http://apt.postgresql.org/pub/repos/apt/ `lsb_release -cs`-pgdg main" >> /etc/apt/sources.list.d/pgdg.list'
wget -q https://www.postgresql.org/media/keys/ACCC4CF8.asc -O - | sudo apt-key add -

sudo apt-get update
sudo apt-get install -y postgresql postgresql-contrib

echo "------------- [OK] Postgres установлен -------------"
echo "------------- [  ] Java установка      -------------"

sudo apt-get install -y openjdk-9-jdk-headless
sudo apt-get install -y maven

echo "------------- [OK] Java установлена    -------------"
./rundb.sh
echo "------------- [  ] Сборка проекта      -------------"
cd spring-boot
mvn clean package
cd ../
echo "------------- [OK] Проект собран       -------------"
echo "----------------- СТАРТ ПРИЛОЖЕНИЯ -----------------"
java -Xmx300M -Xmx300M -jar spring-boot/target/database-docker.jar