# Tut-and-Tam

Минимальный backend (Java 17 + Spring Boot + PostgreSQL/H2) для проекта туроператора.

## Что реализовано

- ER-модель в JPA-сущностях: Contracts, MemberList, Tourists, Tourists_Groups, Groups, Tours, TourJournal, Events, Manager, Guides, Feedback.
- API для чат-бота:
  - Проверка контракта и выдача списка участников (до 8 кнопок/туристов).
  - Авторизация гида и менеджера по email/password.
  - Просмотр активных туров (публичный каталог).
  - Турист: моя группа, мой тур, связь с гидом, отправка сообщения в поддержку, отправка отзыва после завершения тура.
  - Гид: моя группа, массовая рассылка по своей группе.
- API для веб-клиента менеджера:
  - Реестр текущих групп.
  - Таймлайн/цепочка мероприятий тура со статус-цветом (`green`/`red`/`blue`).
  - Выгрузка отчетов в Excel-совместимом CSV-формате:
    - по одному туру: `/api/manager/reports/tour/{tourId}`
    - по периоду и списку туров: `/api/manager/reports/period?from=YYYY-MM-DD&to=YYYY-MM-DD&tourIds=id1,id2`
- Централизованная обработка ошибок и логирование ошибок/предупреждений.

## Запуск

```bash
mvn spring-boot:run
```

По умолчанию используется in-memory H2 в PostgreSQL-режиме.
Для PostgreSQL задайте:

- `DB_URL`
- `DB_USER`
- `DB_PASSWORD`

## Тесты

```bash
mvn test
```
