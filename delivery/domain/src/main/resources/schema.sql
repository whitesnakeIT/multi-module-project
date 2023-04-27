CREATE SCHEMA IF NOT EXISTS test;

DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS couriers;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS delivieries;
DROP TABLE IF EXISTS trackings;

CREATE TABLE customers (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           firstName VARCHAR(255),
                           lastName VARCHAR(255),
                           email VARCHAR(255)
);


CREATE TABLE couriers (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          couriersCompany VARCHAR(255),
                          firstName VARCHAR(255),
                          lastName VARCHAR(255)
);
CREATE TABLE orders (
                        order_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        sender_address VARCHAR(255),
                        receiver_address VARCHAR(255),
                        package_type VARCHAR(255),
                        package_size VARCHAR(255),
                        delivery_date VARCHAR(255)
);