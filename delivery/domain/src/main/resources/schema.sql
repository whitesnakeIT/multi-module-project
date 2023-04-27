CREATE SCHEMA IF NOT EXISTS test;

DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS couriers;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS delivieries;
DROP TABLE IF EXISTS trackings;

CREATE TABLE customers (
                           customer_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           first_name VARCHAR(255),
                           last_name VARCHAR(255),
                           email VARCHAR(255)
);


CREATE TABLE couriers (
                          courier_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          couriers_Company VARCHAR(255),
                          first_name VARCHAR(255),
                          last_name VARCHAR(255)
                          
);
CREATE TABLE orders (
                        order_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        sender_address VARCHAR(255),
                        receiver_address VARCHAR(255),
                        package_type VARCHAR(255),
                        package_size VARCHAR(255),
                        preferred_delivery_date VARCHAR(255),
                        customer_id INT NOT NULL,
                        CONSTRAINT FK_CustomerOrder FOREIGN KEY (customer_id)
                            REFERENCES customers(customer_id)

);