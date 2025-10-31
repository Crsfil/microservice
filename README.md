## Interview Practice Microservices

Набор маленьких сервисов на **Spring Boot + Maven + Lombok + Kafka + JPA**, содержащий намеренно допущенные ошибки. Проект можно использовать как песочницу перед собеседованием: запустить тесты, убедиться, что они падают, и последовательно исправить типичные проблемы со спрингом.

### Как запускать тесты
- Собрать всё сразу: `mvn clean test`  
- По отдельным сервисам:
  - `mvn -pl order-service test`
  - `mvn -pl inventory-service test`
  - `mvn -pl payment-service test`

Kafka поднимается через `docker-compose.yml` (zookeeper + kafka). Для самих тестов Kafka не требуется, т.к. используется `@MockBean`, но пригодится для ручных проверок.

### Что нужно починить

1. `order-service`  
   - Класс: `order-service/src/main/java/com/example/orderservice/service/OrderService.java`  
   - Симптом: тест `order-service/src/test/java/com/example/orderservice/OrderServiceTransactionalTest.java` падает, потому что заказ остаётся в базе, даже если публикация события в Kafka не удалась.  
   - Подсказка: проваленные транзакции и rollback — частый вопрос на собеседованиях.

2. `inventory-service`  
   - Класс: `inventory-service/src/main/java/com/example/inventoryservice/service/InventoryService.java`  
   - Симптом: тест `inventory-service/src/test/java/com/example/inventoryservice/InventoryServiceReservationTest.java` показывает, что после резервирования количество товара не уменьшается.  
   - Подсказка: внимательно посмотреть на аннотации `@Transactional`.

3. `payment-service`  
   - Классы: `payment-service/src/main/java/com/example/paymentservice/config/KafkaConfig.java`, `payment-service/src/main/java/com/example/paymentservice/service/PaymentService.java`  
   - Симптом: тест `payment-service/src/test/java/com/example/paymentservice/PaymentServiceContextTest.java` не поднимает контекст из-за проблем с внедрением `KafkaTemplate`.  
   - Подсказка: рассогласование generic-типов бинов и их внедрения.

Эти задачи покрывают типовые темы для live-debug / live-coding:
- корректная работа транзакций и взаимодействие с репозиториями Spring Data;
- настройка и режимы `@Transactional`;
- проблемы `@Autowired`, `@Qualifier`, generic-типов и конфигурационных бинов.

Дополнительно можно усложнять практику:
- добавить negative-тесты на новые edge cases;
- покрыть бизнес-слой unit-тестами (используя `@DataJpaTest`, `@MockBean` и т.д.);
- расширить взаимодействие между сервисами через Kafka.

Удачи в подготовке!

### Дополнительно (что ещё нужно починить)

4. Репозиторий засорён артефактами  
   - Файлы: `.gitignore`, каталоги `**/target/**`, файлы `*.iml`.  
   - Симптом: в истории коммитов и `git status` видны build-артефакты и IDE-файлы.  
   - Подсказка: добавить типовой `.gitignore` (Maven/IntelliJ) и удалить артефакты из VCS.

5. Профили для БД и JPA  
   - Файлы: `order-service/src/main/resources/application.yml`, `inventory-service/src/main/resources/application.yml`.  
   - Симптом: H2 и `spring.jpa.hibernate.ddl-auto=update` заданы по умолчанию — риск уехать в “прод”.  
   - Подсказка: вынести настройки БД и JPA в `application-dev.yml`/`application-test.yml`; тестам добавить `@ActiveProfiles("test")`.

6. Жёстко захардкоженные Kafka endpoints  
   - Классы: `order-service/.../config/KafkaConfig.java`, `payment-service/.../config/KafkaConfig.java`.  
   - Симптом: сервисы в Docker пытаются подключиться к `localhost:9092` вместо `kafka:29092`.  
   - Подсказка: читать `spring.kafka.bootstrap-servers`; добавить профиль `docker` с `kafka:29092`; в `dev` — `localhost:9092`.

7. Возврат DTO вместо JPA-сущностей  
   - Классы: `order-service/.../controller/OrderController.java`, `inventory-service/.../controller/InventoryController.java`.  
   - Симптом: API “протекает” внутренними полями JPA, возможны проблемы версионирования.  
   - Подсказка: добавить DTO (например, `OrderResponse`, `InventoryItemResponse`) и маппинг из сущностей.

8. Глобальная обработка ошибок (inventory-service)  
   - Класс: `inventory-service/.../service/InventoryService.java` (источник исключений).  
   - Симптом: `EntityNotFoundException`/`IllegalArgumentException` превращаются в 500 вместо 404/400.  
   - Подсказка: `@RestControllerAdvice` + `@ExceptionHandler` для маппинга на корректные статусы.

9. Согласовать JSON-сериализацию событий  
   - Классы: `order-service/.../config/KafkaConfig.java`, `payment-service/.../config/KafkaConfig.java`.  
   - Симптом: различается политика `JsonSerializer` (type headers), что усложняет межсервисную совместимость.  
   - Подсказка: единообразно включить/выключить `JsonSerializer.ADD_TYPE_INFO_HEADERS` во всех сервисах.

10. Блокирующие вызовы внутри транзакции  
   - Класс: `order-service/.../service/OrderService.java`.  
   - Симптом: `kafkaTemplate.send(...).get()` выполняется внутри `@Transactional`, транзакция ждёт сеть/брокер.  
   - Подсказка: публикация после commit (TransactionSynchronization) или шаблон outbox; исключения мэппить без `InterruptedException` swallowing.

11. Неоднозначность Kafka-бинов  
   - Классы/конфиги: `order-service`, `payment-service`.  
   - Симптом: одновременное использование автоконфигурации `spring.kafka.*` и кастомных бинов может породить 2 `KafkaTemplate` в контексте.  
   - Подсказка: либо полагаться на автоконфигурацию, либо оставлять кастомные бины и убирать дубли; при необходимости использовать `@Qualifier`.

12. Явные DTO для создания товаров  
   - Класс: `inventory-service/.../controller/InventoryController.java`.  
   - Симптом: для создания используется `ReserveStockRequest`, что смешивает контракт создания и резервирования.  
   - Подсказка: завести отдельный `CreateInventoryItemRequest` для POST создания и использовать `ReserveStockRequest` только для `/reserve`.
