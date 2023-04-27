package com.kapusniak.tomasz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

//    @Bean
//    public CommandLineRunner run(OrderService orderService) {
//        return (String[] args) -> {
//            Order order1 = new Order();
//            order1.setPreferredDeliveryDate(LocalDate.of(2023, 5, 4));
//            order1.setPackageSize(PackageSize.LARGE);
//            order1.setPackageType(PackageType.DOCUMENT);
//            order1.setSenderAddress("new sender address");
//            order1.setReceiverAddress("new receiver address");
//            Customer customer = new Customer();
//            customer.setId(5L);
//            order1.setCustomer(customer);
//            orderService.save(order1);
//            orderService.findAll().forEach(System.out::println);
//        };
//    }

}