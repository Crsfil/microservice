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
