package com.kapusniak.tomasz.service;

import com.kapusniak.tomasz.entity.Customer;
import com.kapusniak.tomasz.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
//@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;

    public void save(Customer customer) {
        if (customer == null) {
            throw new RuntimeException("Saving customer failed. Customer is null.");
        }
        customerRepository.save(customer);
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Customer findById(Long customerId) {
        if (customerId == null) {
            throw new RuntimeException("Searching for customer failed. Customer id is null.");
        }
        return customerRepository.findById(customerId)
                .orElseThrow(RuntimeException::new);
    }

    public void delete(Long customerId) {
        if (customerId == null) {
            throw new RuntimeException("Deleting customer failed. Customer id is null.");
        }
        Customer customer = findById(customerId);
        customerRepository.delete(customer);
    }

}
