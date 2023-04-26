package com.kapusniak.tomasz;

import com.kapusniak.tomasz.entity.Order;
import com.kapusniak.tomasz.enums.PackageSize;
import com.kapusniak.tomasz.enums.PackageType;
import com.kapusniak.tomasz.service.OrderService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

    }


    @Bean
    public CommandLineRunner run(OrderService orderService) {
        return (String[] args) -> {
            Order order1 = new Order();
            order1.setDeliveryDate(LocalDate.of(2023, 5, 4));
            order1.setPackageSize(PackageSize.LARGE);
            order1.setPackageType(PackageType.DOCUMENT);
            order1.setSenderAddress("new sender address");
            order1.setReceiverAddress("new receiver address");
            orderService.save(order1);
            orderService.findAll().forEach(System.out::println);
        };
    }

}