# Лабораторная работа #7

## Задание
Для получения задания введите номер вашего варианта.

## Порядок выполнения работы:
1. Использовать **PostgreSQL** в качестве базы данных.
2. Для подключения к БД на кафедральном сервере:
   - Хост: `pg`
   - Имя базы данных: `studs`
   - Имя пользователя/пароль совпадают с учётными данными для подключения к серверу.

## Отчёт по работе должен содержать:
1. Текст задания.
2. Диаграмма классов разработанной программы.
3. Исходный код программы.
4. Выводы по работе.

## Вопросы к защите:
1. Многопоточность: класс `Thread`, интерфейс `Runnable`, модификатор `synchronized`.
2. Методы `wait()`, `notify()` класса `Object`, интерфейсы `Lock` и `Condition`.
3. Классы-синхронизаторы из пакета `java.util.concurrent`.
4. Модификатор `volatile`. Атомарные типы данных и операции.
5. Коллекции из пакета `java.util.concurrent`.
6. Интерфейсы `Executor`, `ExecutorService`, `Callable`, `Future`.
7. Пулы потоков.
8. **JDBC**: порядок взаимодействия с БД, класс `DriverManager`, интерфейс `Connection`.
9. Интерфейсы `Statement`, `PreparedStatement`, `ResultSet`, `RowSet`.
10. Шаблоны проектирования.

---

# Лабораторная работа #8

## Задание
Доработать программу из **ЛР №7**, заменив консольный клиент на **GUI** с функционалом:

### Основные требования:
1. **Окно авторизации/регистрации**.
2. Отображение текущего пользователя.
3. **Таблица**:
   - Все объекты коллекции.
   - Каждое поле — отдельная колонка.
   - Фильтрация/сортировка через **Streams API**.
4. **Визуализация объектов**:
   - Отрисовка графическими примитивами (`Graphics`, `Canvas` и др.).
   - Разные цвета для объектов разных пользователей.
   - Информация об объекте при клике.
   - Автоматическое обновление при изменениях (добавление/удаление/изменение).
   - Анимация (согласованная с преподавателем).
5. **Редактирование**:
   - Возможность редактирования полей объектов (только своих).
   - Удаление выбранного объекта.
6. **Прототип интерфейса** (согласовать с преподавателем, использовать `mockplus`, `draw.io` и т. д.).

## Вопросы к защите:
1. Компоненты пользовательского интерфейса. Иерархия компонентов.
2. Базовые классы: `Component`, `Container`, `JComponent`.
3. Менеджеры компоновки.
4. Модель обработки событий: класс-слушатель и класс-событие.
5. **JavaFX**: архитектура, отличия от AWT/Swing.
6. Интернационализация и локализация. Хранение ресурсов.
7. Форматирование данных: `NumberFormat`, `DateFormat`, `MessageFormat`, `ChoiceFormat`.