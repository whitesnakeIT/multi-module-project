package com.kapusniak.tomasz.controller;

import com.kapusniak.tomasz.entity.CustomerEntity;
import com.kapusniak.tomasz.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public List<CustomerEntity> getAllCustomers() {
        return customerService.findAll();
    }

    @GetMapping("/{id}")
    public CustomerEntity getCustomer(@PathVariable Long id) {
        return customerService.findById(id);
    }

    @PostMapping
    public ResponseEntity<CustomerEntity> createCustomer(@RequestBody CustomerEntity customer) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(customerService.save(customer));
    }

    @PutMapping("/{id}")
    public CustomerEntity updateCustomer(@PathVariable Long id, @RequestBody CustomerEntity customer) {
        return customerService.update(id, customer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}