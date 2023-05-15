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

    public Customer save(Customer customer) {
        if (customer == null) {
            throw new RuntimeException("Saving customer failed. Customer is null.");
        }
        return customerRepository.save(customer);
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

    public Customer update(Long id, Customer customer) {
        if (id == null) {
            throw new RuntimeException("Updating customer failed. Customer id is null.");
        }
        if (customer == null) {
            throw new RuntimeException("Updating customer failed. Customer is null.");
        }

        Customer customerFromDb = findById(id);
        customerFromDb.setEmail(customer.getEmail());
        customerFromDb.setFirstName(customer.getFirstName());
        customerFromDb.setLastName(customer.getLastName());

        return customerRepository.save(customerFromDb);
    }


}
