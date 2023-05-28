INSERT INTO customers (first_name, last_name, email, uuid)
VALUES ('Adam', 'Nowak', 'adam.nowak@example.com', '28f60dc1-993a-4d08-ac54-850a1fefb6a3'),
       ('Monika', 'Kowalska', 'monika.kowalska@example.com', '61ab1a58-9653-4c9c-9ec2-84347152b08a'),
       ('Piotr', 'Wiśniewski', 'piotr.wisniewski@example.com', '03cbd3ae-c717-460a-abf4-0f2fa4067644'),
       ('Maria', 'Lewandowska', 'maria.lewandowska@example.com', 'ca7487dc-d462-4cec-8fec-57de865d58cd'),
       ('Tomasz', 'Wójcik', 'tomasz.wojcik@example.com', 'f0f70f4e-b76f-4e4c-b8c7-65da76b68552');


INSERT INTO couriers (courier_company, first_name, last_name, uuid)
VALUES ('DHL', 'Jan', 'Kowalski', 'fe362772-17c3-4547-b559-ceb13e164e6f'),
       ('DPD', 'Anna', 'Nowak', '7ded2601-a101-4958-8924-130a17278a20'),
       ('UPS', 'Michał', 'Wójcik', 'a382f6da-cf7d-4cbf-b2f6-58cdaa420d7b'),
       ('FEDEX', 'Katarzyna', 'Lewandowska', 'c402c044-bf8b-488f-8f37-0e6a4c6b0f72'),
       ('GLS', 'Piotr', 'Szymański', '9cfcacfd-2189-4344-bb20-878459d367a0');

INSERT INTO orders (sender_address, receiver_address, package_type, package_size, preferred_delivery_date, uuid,
                    customer_id)
VALUES ('ul. Kowalska 1, Warszawa', 'ul. Nowa 2, Kraków', 'DOCUMENT', 'SMALL', '2023-05-02',
        '29755321-c483-4a12-9f64-30a132038b70', 1),
       ('ul. Nowa 3, Gdańsk', 'ul. Kowalska 2, Poznań', 'PARCEL', 'MEDIUM', '2023-05-03',
        '9137383a-1574-4981-bf7e-3b05182fcf13', 1),
       ('ul. Główna 4, Wrocław', 'ul. Słoneczna 5, Katowice', 'DOCUMENT', 'SMALL', '2023-05-04',
        '06a4084b-5837-4303-ab5a-8b50fedb3898', 1),
       ('ul. Cicha 6, Łódź', 'ul. Wąska 7, Lublin', 'PARCEL', 'LARGE', '2023-05-05',
        '9266d57c-9594-4f3b-8914-572ee4202e96', 2),
       ('ul. Piękna 8, Szczecin', 'ul. Zielona 9, Rzeszów', 'DOCUMENT', 'EXTRA_LARGE', '2023-05-06',
        'feabd37b-1cfc-44d7-badc-3f775fb94975', 2);

INSERT INTO deliveries (delivery_id, delivery_status, price, delivery_time, uuid, courier_id, order_id)
VALUES (1, 'CREATED', 20.00, '2022-04-01 12:00:00', '31822712-94b3-43ed-9aac-24613948ca79', 1, 1),
       (2, 'PICKED_UP', 30.00, '2022-04-02 10:00:00', '1f263424-a92a-49a6-b38f-eaa2861ab332', 2, 2),
       (3, 'IN_TRANSIT', 30.00, '2022-04-03 14:00:00', 'e0f6741d-3ef9-49b2-9f6c-072898933246', 4, 3),
       (4, 'IN_TRANSIT', 25.00, '2022-04-04 16:00:00', 'd4cd6aca-b895-4aeb-a882-6409d6c2975f', 2, 4),
       (5, 'DELIVERED', 25.00, '2022-04-04 18:00:00', 'cb89dfad-db01-4dd0-9df9-61db335308d8', 5, 5);

INSERT INTO tracking (tracking_id, localization, uuid)
VALUES (1, 'Location 1', '97e37668-b355-4ecd-83be-dbc9cf56d8c0'),
       (2, 'Location 2', '1bfb22e4-2dfa-4b92-a7a3-fe35c948216c'),
       (3, 'Location 3', '9d615769-c925-4790-bb6d-69ad31daf07e'),
       (4, 'Location 4', '44ca6b9c-a5cb-43a4-ad7e-51563f96d581'),
       (5, 'Location 5', '670950c2-8dba-4c4b-a334-a9df91a485cb');