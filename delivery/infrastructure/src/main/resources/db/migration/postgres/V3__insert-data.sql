
INSERT INTO customers (first_name, last_name, email)
VALUES ('Adam', 'Nowak', 'adam.nowak@example.com'),
       ('Monika', 'Kowalska', 'monika.kowalska@example.com'),
       ('Piotr', 'Wiśniewski', 'piotr.wisniewski@example.com'),
       ('Maria', 'Lewandowska', 'maria.lewandowska@example.com'),
       ('Tomasz', 'Wójcik', 'tomasz.wojcik@example.com');


INSERT INTO couriers (courier_company, first_name, last_name)
VALUES ('DHL', 'Jan', 'Kowalski'),
       ('DPD', 'Anna', 'Nowak'),
       ('UPS', 'Michał', 'Wójcik'),
       ('FEDEX', 'Katarzyna', 'Lewandowska'),
       ('GLS', 'Piotr', 'Szymański');

INSERT INTO orders (sender_address, receiver_address, package_type, package_size, preferred_delivery_date, customer_id)
VALUES ('ul. Kowalska 1, Warszawa', 'ul. Nowa 2, Kraków', 'DOCUMENT', 'SMALL', '2023-05-02', 1),
       ('ul. Nowa 3, Gdańsk', 'ul. Kowalska 2, Poznań', 'PARCEL', 'MEDIUM', '2023-05-03', 1),
       ('ul. Główna 4, Wrocław', 'ul. Słoneczna 5, Katowice', 'DOCUMENT', 'SMALL', '2023-05-04', 1),
       ('ul. Cicha 6, Łódź', 'ul. Wąska 7, Lublin', 'PARCEL', 'LARGE', '2023-05-05', 2),
       ('ul. Piękna 8, Szczecin', 'ul. Zielona 9, Rzeszów', 'DOCUMENT', 'EXTRA_LARGE', '2023-05-06', 2);

INSERT INTO deliveries (delivery_id, delivery_status, price, delivery_time, courier_id, order_id)
VALUES (1, 'CREATED', 20.00, '2022-04-01 12:00:00', 1, 1),
       (2, 'PICKED_UP', 30.00, '2022-04-02 10:00:00', 2, 2),
       (3, 'IN_TRANSIT', 30.00, '2022-04-03 14:00:00', 4, 3),
       (4, 'IN_TRANSIT', 25.00, '2022-04-04 16:00:00', 2, 4),
       (5, 'DELIVERED', 25.00, '2022-04-04 18:00:00', 5, 5);

INSERT INTO tracking (tracking_id, localization)
VALUES (1, 'Location 1'),
       (2, 'Location 2'),
       (3, 'Location 3'),
       (4, 'Location 4'),
       (5, 'Location 5');