package com.kapusniak.tomasz.controller;

import com.kapusniak.tomasz.entity.Customer;
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
    public List<Customer> getAllCustomers() {
        return customerService.findAll();
    }

    @GetMapping("/{id}")
    public Customer getCustomer(@PathVariable Long id) {
        return customerService.findById(id);
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(customerService.save(customer));
    }

    @PutMapping("/{id}")
    public Customer updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
        return customerService.update(id, customer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}