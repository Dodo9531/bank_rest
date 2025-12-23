Запуск локально
1) Открыть application.yml и изменить параметры для подключения к базе данных, а точнее datasource.url, datasource.username, datasource.password чтобы приложение подключилось к вашей базе данных
2) Собрать проект (например в терминале командой mvn clean install)
3) Запустить 
4) Проверить запущено ли приложение можно по адресу http://localhost:8080/swagger-ui/index.html


Поднятие в докере
1) Собрать проект (например в терминале командой mvn clean install)
2) Запускаем докер демона
3) Выполняем в терминале "docker build -t bank-rest-service ." для создания образа в докере
4) Поднимаем контейнеры (либо запуском docker-compose.yml в idea либо командой в терминале docker-compose up -d)
5) Проверить запущено ли приложение можно по адресу http://localhost:8080/swagger-ui/index.html