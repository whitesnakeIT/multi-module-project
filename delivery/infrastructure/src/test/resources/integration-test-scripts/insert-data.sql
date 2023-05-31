INSERT INTO "delivery_integration_tests".customers (customer_id, first_name, last_name, email, uuid)
VALUES (1, 'Adam', 'Nowak', 'adam.nowak@example.com', '28f60dc1-993a-4d08-ac54-850a1fefb6a3'),
       (2, 'Monika', 'Kowalska', 'monika.kowalska@example.com', '61ab1a58-9653-4c9c-9ec2-84347152b08a');


INSERT INTO "delivery_integration_tests".couriers (courier_id, courier_company, first_name, last_name, uuid)
VALUES (1, 'DHL', 'Jan', 'Kowalski', 'fe362772-17c3-4547-b559-ceb13e164e6f'),
       (2, 'DPD', 'Anna', 'Nowak', '7ded2601-a101-4958-8924-130a17278a20');

INSERT INTO "delivery_integration_tests".orders (order_id, sender_address, receiver_address, package_type, package_size,
                                                 preferred_delivery_date, uuid,
                                                 customer_id)
VALUES (1, 'ul. Kowalska 1, Warszawa', 'ul. Nowa 2, Kraków', 'DOCUMENT', 'SMALL', '2023-05-02',
        '29755321-c483-4a12-9f64-30a132038b70', 1),
       (2, 'ul. Nowa 3, Gdańsk', 'ul. Kowalska 2, Poznań', 'PARCEL', 'MEDIUM', '2023-05-03',
        '9137383a-1574-4981-bf7e-3b05182fcf13', 1);


INSERT INTO "delivery_integration_tests".deliveries (delivery_id, delivery_status, price, delivery_time, uuid,
                                                     courier_id, order_id)
VALUES (1, 'CREATED', 20.00, '2022-04-01 12:00:00', '31822712-94b3-43ed-9aac-24613948ca79', 1, 1),
       (2, 'PICKED_UP', 30.00, '2022-04-02 10:00:00', '1f263424-a92a-49a6-b38f-eaa2861ab332', 2, 2);

INSERT INTO "delivery_integration_tests".tracking (tracking_id, localization, uuid)
VALUES (1, 'Location 1', '97e37668-b355-4ecd-83be-dbc9cf56d8c0'),
       (2, 'Location 2', '1bfb22e4-2dfa-4b92-a7a3-fe35c948216c');
