Чтобы взаимодействовать с Kafka-брокерами, будем использовать инструмент командной строки **kcat**.

Можно установить **kcat** локально с помощью **Homebrew**:
`brew install kcat`

Или с помощью **docker-контейнера**: `docker run --rm -it edenhill/kcat -b localhost:29092 -t order-payments -C`

Для просмотра сообщений в нашем случае будем использовать следующие команды:
- `kcat -b localhost:29092 -t order-payments -C` для успешных платежей;
- `kcat -b localhost:29092 -t failed-payments -C` для неудачных платежей.