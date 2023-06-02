TRUNCATE TABLE "delivery_integration_tests".couriers CASCADE;
TRUNCATE TABLE "delivery_integration_tests".customers CASCADE;
TRUNCATE TABLE "delivery_integration_tests".deliveries CASCADE;
TRUNCATE TABLE "delivery_integration_tests".orders CASCADE;
TRUNCATE TABLE "delivery_integration_tests".tracking CASCADE;

ALTER SEQUENCE "delivery_integration_tests".couriers_courier_id_seq RESTART WITH 3;
ALTER SEQUENCE "delivery_integration_tests".customers_customer_id_seq RESTART WITH 3;
ALTER SEQUENCE "delivery_integration_tests".deliveries_delivery_id_seq RESTART WITH 3;
ALTER SEQUENCE "delivery_integration_tests".orders_order_id_seq RESTART WITH 3;
ALTER SEQUENCE "delivery_integration_tests".tracking_tracking_id_seq RESTART WITH 3;

