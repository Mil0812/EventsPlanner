--Заповнення даними таблиці "Користувач"
INSERT INTO users(id, login, password, firstName, lastName, email, status)
VALUES ('ce66389c-ab6e-42d8-aa79-b6b9abf41cdb', 'lollipop321', 'lily098', 'Фанні', 'Марвен',
        'lollipop@i.ua', 'student'),
       ('93966090-3acc-41c2-a88c-1808fbcb60a1', 'millenium', 'Mmm7', 'Міа', 'Лейм',
        'mia-leim1@gmail.com', 'student'),
       ('3b5a1d93-6a0b-42c1-b1c8-25caedec9fe9', 'waX99', 'Wax00', 'Мелорі', 'Джонсон', 'wax@i.ua',
        'teacher'),
       ('e969b133-c007-419b-bd64-3fbba78ecb2d', 'sunny', '777', 'Аделія', 'Хоуп',
        'suunnyyy@gmail.com', 'teacher');

--Заповнення даними таблиці "Розділи"
INSERT INTO section(id, name)
VALUES ('43997949-68b8-4731-8105-ad81280488d8', 'Access'),
       ('c6091036-3bc7-41ae-a01f-2e8d1b66d123', 'Запити'),
       ('5e8e2e90-b5cb-456b-a006-73c8c24542eb', 'Таблиці');

--Заповнення даними таблиці "Тип тесту"
INSERT INTO test_type(id, name, description, title, image, max_answer_count, correct_answer_count)
VALUES ('d4e83df7-0b97-45df-a2f1-f85eba2fcf5f', 'Направо підеш...',
        'Питання з 2 directions, в якому лише один з них є правильним',
        'Оберіть правильну відповідь', NULL, 2, 1),
       ('372a768e-a0e1-4f60-9fcc-6579eb82bc0c', 'Варіантів є декілька',
        'Питання з декількома правильними варіантами відповіді', 'Оберіть правильні відповіді',
        NULL, 4, 2),
       ('9e8c3230-f72a-4f85-a6b3-3c8de39fc42e', 'Тестуємо клавіатуру',
        'Питання, на яке потрібно вписати правильну відповідь вручну',
        'Впишіть правильну відповідь', NULL, 1, 1);

--Заповнення даними таблиці "Тест"
INSERT INTO test(id, author_id, type_id, section_id, title, image, question_count)
VALUES ('b3566029-3256-466a-8582-de168fe65d04',
        '3b5a1d93-6a0b-42c1-b1c8-25caedec9fe9', '372a768e-a0e1-4f60-9fcc-6579eb82bc0c',
        '43997949-68b8-4731-8105-ad81280488d8',
        'Основи Access', NULL, 3),
       ('b445b3da-f6e8-4307-a007-b7563bfa253a',
        'e969b133-c007-419b-bd64-3fbba78ecb2d', 'd4e83df7-0b97-45df-a2f1-f85eba2fcf5f',
        'c6091036-3bc7-41ae-a01f-2e8d1b66d123', 'SQL-запити: база', NULL, 2);

--Заповнення даними таблиці "Питання"
INSERT INTO question(id, question_text, test_id)
VALUES ('ba1ea871-4226-4669-b769-94a940d9d3b2', 'Основними елементами СУБД Access є:',
        'b3566029-3256-466a-8582-de168fe65d04'),
       ('eee56e6e-f658-4011-b8b1-47af9ee247a0', 'Яке ключове слово зазвичай стоїть після SELECT?',
        'b445b3da-f6e8-4307-a007-b7563bfa253a');

--Заповнення даними таблиці "Відповідь"
INSERT INTO answer(id, question_id, answer_text, correctness)
VALUES ('c2c99faa-4075-42bd-b336-d764559e9dc4', 'ba1ea871-4226-4669-b769-94a940d9d3b2', 'Таблиці',
        true),
       ('984c7a54-59ed-4c9b-856f-9a540589b0f1', 'ba1ea871-4226-4669-b769-94a940d9d3b2', 'Запити',
        true),
       ('06cb69e2-35a5-4abf-a9ce-dcc5894d5634', 'ba1ea871-4226-4669-b769-94a940d9d3b2', 'Анотації',
        false),
       ('35730f6c-fdea-43f3-a5da-026d80d9293c', 'ba1ea871-4226-4669-b769-94a940d9d3b2', 'Кнопки',
        false),
       ('556c0a6f-fc25-4934-ab00-0b3354b2249f', 'eee56e6e-f658-4011-b8b1-47af9ee247a0', 'HAVING',
        false),
       ('a44bd139-903a-4d5e-ad11-6c1d17538ca2', 'eee56e6e-f658-4011-b8b1-47af9ee247a0', 'FROM',
        true);