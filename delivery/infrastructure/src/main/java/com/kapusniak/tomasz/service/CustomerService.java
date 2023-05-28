package com.kapusniak.tomasz.service;

import com.kapusniak.tomasz.entity.CustomerEntity;
import com.kapusniak.tomasz.mapstruct.CustomerEntityMapper;
import com.kapusniak.tomasz.openapi.model.Customer;
import com.kapusniak.tomasz.repository.jpa.CustomerJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerService {

    private final CustomerJpaRepository customerRepository;

    private final CustomerEntityMapper customerEntityMapper;

    public Customer save(Customer customer) {
        if (customer == null) {
            throw new RuntimeException("Saving customer failed. Customer is null.");
        }
        CustomerEntity customerEntity = customerEntityMapper.mapToEntity(customer);
        CustomerEntity savedEntity = customerRepository.save(customerEntity);

        return customerEntityMapper.mapToApiModel(savedEntity);
    }

    public List<Customer> findAll() {
        return customerRepository
                .findAll()
                .stream()
                .map(customerEntityMapper::mapToApiModel)
                .toList();
    }

    public Customer findById(Long customerId) {
        if (customerId == null) {
            throw new RuntimeException("Searching for customer failed. Customer id is null.");
        }
        return customerEntityMapper.mapToApiModel(customerRepository.findById(customerId)
                .orElseThrow(RuntimeException::new));
    }

    public void delete(Long customerId) {
        if (customerId == null) {
            throw new RuntimeException("Deleting customer failed. Customer id is null.");
        }
        Customer customer = findById(customerId);

        customerRepository.delete(customerEntityMapper.mapToEntity(customer));
    }

    public Customer update(Long id, Customer customer) {
        if (id == null) {
            throw new RuntimeException("Updating customer failed. Customer id is null.");
        }
        if (customer == null) {
            throw new RuntimeException("Updating customer failed. Customer is null.");
        }

        Customer customerFromDb = findById(id);

        Customer updatedCustomer = updateFields(customerFromDb, customer);

        CustomerEntity updatedCustomerEntity = customerRepository
                .save(customerEntityMapper.mapToEntity(updatedCustomer));

        return customerEntityMapper.mapToApiModel(updatedCustomerEntity);
    }

    private Customer updateFields(Customer customerFromDb, Customer newCustomer) {
        if (newCustomer.getId() == null) {
            newCustomer.setId(customerFromDb.getId());
        }
        if (!newCustomer.getId().equals(customerFromDb.getId())) {
            throw new RuntimeException("Updating customer fields failed. Different id's");
        }
        return newCustomer;
    }

}
