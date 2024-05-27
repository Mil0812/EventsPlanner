--Заповнення даними таблиці "Користувач"
INSERT INTO users(id, login, password, firstName, avatar)
VALUES ('ce66389c-ab6e-42d8-aa79-b6b9abf41cdb', 'lollipop321', 'lily098', 'Фанні', null),
       ('93966090-3acc-41c2-a88c-1808fbcb60a1', 'millennium', 'Miaaa7', 'Міа', null),
       ('3b5a1d93-6a0b-42c1-b1c8-25caedec9fe9', 'waX99', 'Wax00', 'Мелорі', null),
       ('e969b133-c007-419b-bd64-3fbba78ecb2d', 'sunny', '777Iii', 'Аделія', null);

INSERT INTO my_events(id, name, description, date, image, user_id) VALUES
('69a6d374-7edc-4a20-acad-0f2beb85b768', 'День народження мами',
'Ідеальний день! Але з умовою, шо я до того часу зроблю все, шо маю зробити на курсову',
 '2024-06-01', null, '93966090-3acc-41c2-a88c-1808fbcb60a1');

INSERT INTO day_task(id, user_id, name, created_at, completeness) VALUES
('0ac53af1-2092-44e8-8d36-5659df9473c5', '3b5a1d93-6a0b-42c1-b1c8-25caedec9fe9', 'Доробити ОПІ', '2024-05-25 10:00:00', false);