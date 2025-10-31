## Interview Practice Microservices

Много-модульный проект на **Spring Boot + Maven + Kafka + JPA + Liquibase**, предназначенный для тренировок перед собеседованиями. В `baseline-clean` собраны рабочие сервисы, а практические сценарии раскладываются по отдельным веткам и задачам.

### Быстрый старт
1. Склонировать репозиторий, переключиться на ветку `baseline-clean`.
2. Установить зависимости (Java 17+, Maven).
3. Запустить тесты:
   - все модули: `mvn clean test`
   - отдельный модуль: `mvn -pl order-service test` (аналогично для остальных).
4. Для интеграционных проверок запустить инфраструктуру:  
   ```bash
   docker compose up -d postgres kafka zookeeper
   ```
   *Kafka* используется для ручных проверок, в тестах она мокируется.

### Профили и конфигурация
- **H2 (по умолчанию)** — быстрая in-memory БД, `MODE=PostgreSQL` для совместимости SQL. Схемы создаются через Liquibase, Hibernate работает в режиме `validate`.
- **postgres** — подключение к внешней БД. Конфигурация в `application-postgres.yml`, значения переопределяются переменными окружения:
  ```
  ORDER_SERVICE_DATASOURCE_URL=jdbc:postgresql://localhost:5432/microservices?currentSchema=order_service
  ORDER_SERVICE_DATASOURCE_USERNAME=micro
  ORDER_SERVICE_DATASOURCE_PASSWORD=micro
  ```
  Аналогичные переменные есть для остальных сервисов.
- Liquibase master-файлы: `src/main/resources/db/changelog/db.changelog-master.yaml`. Дополнительные фикстуры с контекстом `demo` можно включать через `SPRING_LIQUIBASE_CONTEXTS=demo`.

### Docker Compose
`docker-compose.yml` поднимает:
- `zookeeper`, `kafka` — для обмена событиями;
- `postgres` (порт `5432`, база `microservices`, пользователь/пароль `micro`). Данные хранятся в volume `postgres-data`.

### Практические сценарии
- Актуальный список идей и шаблонов задач лежит в `docs/practice-scenarios.md`.
- Для каждой тренировки заводите ветку от `baseline-clean`, добавляйте «красный» тест, фикс и описание воспроизведения.
- Рекомендуемые темы: транзакции, N+1, миграции, конфигурация Kafka, настройки профилей.

### Стандартные задачи (ветки примеров)
| Сервис | Проблематика | Тест | Темы |
| --- | --- | --- | --- |
| order-service | rollback при сбое Kafka | `OrderServiceTransactionalTest` | `@Transactional`, Kafka publisher |
| inventory-service | корректное резервирование | `InventoryServiceReservationTest` | `@Transactional`, JPA updates |
| payment-service | конфигурация KafkaTemplate | `PaymentServiceContextTest` | Spring context, generics |

Используйте эти примеры как отправную точку, затем дополняйте новыми сценариями из списка.

### Полезные шаги после фикса
- Добавить негативные тесты и edge cases.
- Прогнать `mvn -pl <module> test` для затронутого сервиса.
- При необходимости подготовить миграцию Liquibase и обновить документацию.

Удачи в подготовке!
