use test;

DROP TABLE IF EXISTS couriers cascade;
DROP TABLE IF EXISTS customers cascade;
DROP TABLE IF EXISTS deliveries cascade;
DROP TABLE IF EXISTS orders cascade;
DROP TABLE IF EXISTS tracking cascade;


CREATE TABLE couriers
(
    courier_id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    courier_company VARCHAR(255),
    first_name      VARCHAR(255),
    last_name       VARCHAR(255)
);

CREATE TABLE customers
(
    customer_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name  VARCHAR(255),
    last_name   VARCHAR(255),
    email       VARCHAR(255)
);


CREATE TABLE orders
(
    order_id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    sender_address          VARCHAR(255),
    receiver_address        VARCHAR(255),
    package_type            VARCHAR(255),
    package_size            VARCHAR(255),
    preferred_delivery_date date,
    customer_id             BIGINT NOT NULL,
    CONSTRAINT FK_CustomerOrder
        FOREIGN KEY (customer_id)
            REFERENCES test.customers (customer_id)

);

CREATE TABLE tracking
(
    tracking_id BIGINT AUTO_INCREMENT PRIMARY KEY
);

CREATE TABLE deliveries
(
    delivery_id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    delivery_status VARCHAR(255),
    delivery_time   TIMESTAMP(6),
    price           NUMERIC(38, 2),
    courier_id      BIGINT,
    order_id        BIGINT,
    CONSTRAINT FK_Delivery_Courier
        FOREIGN KEY (courier_id)
            REFERENCES test.couriers (courier_id),
    CONSTRAINT FK_Delivery_Order
        FOREIGN KEY (order_id)
            REFERENCES test.orders (order_id)

);