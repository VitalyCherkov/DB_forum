# DB forum

## Docker

1. Собрать

    `docker build -t v.cherkov .`

2. Запустить

    `docker run -p 5000:5000 --name v.cherkov -t v.cherkov`

3. Остановить

    `docker stop v.cherkov && docker rm v.cherkov`

4. Если нужно полностью очистить

    `docker system prune -a`

## Запуск БД без докера

1. Запустить / перезапустить БД, стерев данные

    `./rundb.sh`

2. Остановить БД

    `./stopdb.sh`
