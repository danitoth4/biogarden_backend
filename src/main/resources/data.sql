INSERT INTO crops ( name, image_url, width, length, user_id, type, description)
VALUES ( 'Carrot', 'https://localhost:8080/images/carrot.png', 3, 3, 'admin', 2, 'The carrot (Daucus carota subsp. sativus) is a root vegetable, usually orange in colour.'),
       ( 'Bean', 'https://localhost:8080/images/bean.png', 8, 8, 'admin', 3, 'A bean is a seed of one of several genera of the flowering plant family Fabaceae, which are used for human or animal food.'),
       ( 'Cucumber', 'https://localhost:8080/images/cucumber.png', 5, 5, 'admin', 1, 'Cucumber (Cucumis sativus) is a widely cultivated plant in the gourd family, Cucurbitaceae. It is a creeping vine that bears cucumiform fruits that are used as vegetables'),
       ( 'Potato', 'https://localhost:8080/images/potato.png', 7, 7, 'admin', 2, ''),
       ( 'Garlic', 'https://localhost:8080/images/garlic.png', 2, 2, 'admin', 2, ''),
       ( 'Cabbage', 'https://localhost:8080/images/cabbage.png', 7, 7, 'admin', 0, ''),
       ( 'Tomato', 'https://localhost:8080/images/tomato.png', 7, 7, 'admin', 1, ''),
       ( 'Spinach', 'https://localhost:8080/images/spinach.png', 5, 5, 'admin', 0, ''),
       ( 'Radish', 'https://localhost:8080/images/radish.png', 3, 3, 'admin', 2, ''),
       ( 'Beetroot', 'https://localhost:8080/images/beetroot.png', 4, 4, 'admin', 2, '');

INSERT INTO companion ( id, positive, impacted_id, impacting_id)
VALUES (1, 1, 1, 5 ),
       (2,  1, 1, 9 ),
       (3,  1, 1, 6 ),
       (4,  0, 1, 10 ),
       (5, 1, 2, 6 ),
       (6,  1, 2, 7 ),
       (7, 1, 2, 10 ),
       (8, 0, 2, 5 ),
       (9, 1, 3, 2 ),
       (10, 0, 3, 9 ),
       (11, 1, 3, 10 ),
       (12, 0, 4, 3 ),
       (13, 0, 4, 7 ),
       (14, 1, 4, 5 ),
       (15, 1, 4, 8 ),
       (16, 1, 5, 1 ),
       (17, 1, 5, 3 ),
       (18, 0, 5, 2 ),
       (19, 1, 6, 9 ),
       (20, 1, 6, 1 ),
       (21, 1, 6, 7 ),
       (22, 1, 7, 10 ),
       (23, 1, 7, 1 ),
       (24, 0, 7, 3 ),
       (25, 0, 7, 4 ),
       (26, 1, 8, 4 ),
       (27, 1, 8, 7 ),
       (28, 0, 8, 10 ),
       (29, 0, 9, 3 ),
       (30, 1, 9, 2 ),
       (31, 1, 9, 1 ),
       (32, 1, 10, 3 ),
       (33, 1, 10, 6 ),
       (34, 0, 10, 8 ),
       (35, 0, 10, 2 );