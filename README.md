# feedback

feedback news
Система управления новостями

 RESTful web-service, реализующей функционал для работы с
системой управления новостями.
Основные сущности:
- news (новость) содержит поля: id, date, title, text и comments (list).
- comment содержит поля: id, date, text, username и id_news.


 Разработать API согласно подходов REST . Для потенциально
объемных запросов реализовать постраничность. Основые API:
- CRUD для работы с новостью
- CRUD для работы с комментарием
- просмотр списка новостей (с пагинацией)
- просмотр новости с комментариями относящимися к ней (с пагинацией)
- полнотекстовый поиск по различным параметрам



 Spring Security:
- API для регистрации пользователей с ролями admin/journalist/subscriber
- Администратор (role admin) может производить CRUD-операции со всеми
сущностями
- Журналист (role journalist) может добавлять и изменять/удалять только свои
новости
- Подписчик (role subscriber) может добавлять и изменять/удалять только свои
комментарии
- Незарегистрированные пользователи могут только просматривать новости и
комментарии


