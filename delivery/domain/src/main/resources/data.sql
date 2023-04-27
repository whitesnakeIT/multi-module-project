

INSERT INTO customers (first_name, last_name, email)
VALUES
    ('Adam', 'Nowak', 'adam.nowak@example.com'),
    ('Monika', 'Kowalska', 'monika.kowalska@example.com'),
    ('Piotr', 'Wiśniewski', 'piotr.wisniewski@example.com'),
    ('Maria', 'Lewandowska', 'maria.lewandowska@example.com'),
    ('Tomasz', 'Wójcik', 'tomasz.wojcik@example.com');


INSERT INTO couriers (couriers_company, first_name, last_name)
VALUES
    ('DHL', 'Jan', 'Kowalski'),
    ('DPD', 'Anna', 'Nowak'),
    ('UPS', 'Michał', 'Wójcik'),
    ('FEDEX', 'Katarzyna', 'Lewandowska'),
    ('GLS', 'Piotr', 'Szymański');

INSERT INTO orders (sender_address, receiver_address, package_type, package_size, preferred_delivery_date, customer_id)
VALUES
    ('ul. Kowalska 1, Warszawa', 'ul. Nowa 2, Kraków', 'DOCUMENT', 'SMALL', '2023-05-02', 1),
    ('ul. Nowa 3, Gdańsk', 'ul. Kowalska 2, Poznań', 'PARCEL', 'MEDIUM', '2023-05-03', 1),
    ('ul. Główna 4, Wrocław', 'ul. Słoneczna 5, Katowice', 'DOCUMENT', 'SMALL', '2023-05-04', 1),
    ('ul. Cicha 6, Łódź', 'ul. Wąska 7, Lublin', 'PARCEL', 'LARGE', '2023-05-05',2),
    ('ul. Piękna 8, Szczecin', 'ul. Zielona 9, Rzeszów', 'DOCUMENT', 'EXTRA_LARGE', '2023-05-06', 2);
