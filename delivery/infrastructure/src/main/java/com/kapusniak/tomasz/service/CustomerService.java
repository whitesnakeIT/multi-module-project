package com.kapusniak.tomasz.service;

import com.kapusniak.tomasz.entity.CustomerEntity;
import com.kapusniak.tomasz.repository.jpa.CustomerJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
//@Transactional
public class CustomerService {

    private final CustomerJpaRepository customerRepository;

    public CustomerEntity save(CustomerEntity customer) {
        if (customer == null) {
            throw new RuntimeException("Saving customer failed. Customer is null.");
        }
        return customerRepository.save(customer);
    }

    public List<CustomerEntity> findAll() {
        return customerRepository.findAll();
    }

    public CustomerEntity findById(Long customerId) {
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
        CustomerEntity customer = findById(customerId);
        customerRepository.delete(customer);
    }

    public CustomerEntity update(Long id, CustomerEntity customer) {
        if (id == null) {
            throw new RuntimeException("Updating customer failed. Customer id is null.");
        }
        if (customer == null) {
            throw new RuntimeException("Updating customer failed. Customer is null.");
        }

        CustomerEntity customerFromDb = findById(id);
        customerFromDb.setEmail(customer.getEmail());
        customerFromDb.setFirstName(customer.getFirstName());
        customerFromDb.setLastName(customer.getLastName());

        return customerRepository.save(customerFromDb);
    }


}
