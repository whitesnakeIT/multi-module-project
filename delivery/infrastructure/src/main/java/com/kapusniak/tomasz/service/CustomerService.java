package com.kapusniak.tomasz.service;

import com.kapusniak.tomasz.entity.CustomerEntity;
import com.kapusniak.tomasz.mapper.CustomerEntityMapper;
import com.kapusniak.tomasz.openapi.model.Customer;
import com.kapusniak.tomasz.repository.jpa.CustomerJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerService {

    private final CustomerJpaRepository customerRepository;

    private final CustomerEntityMapper customerEntityMapper;

    @Transactional
    public Customer save(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Saving customer failed. Customer is null.");
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

    public Customer findByUuid(UUID customerUuid) {
        if (customerUuid == null) {
            throw new EntityNotFoundException("Searching for customer failed. Customer uuid is null.");
        }
        return customerEntityMapper.mapToApiModel(customerRepository.findByUuid(customerUuid)
                .orElseThrow(() ->
                        new EntityNotFoundException("Searching for customer failed. Unrecognized uuid " + customerUuid)));
    }

    @Transactional
    public void delete(UUID customerUuid) {
        if (customerUuid == null) {
            throw new IllegalArgumentException("Deleting customer failed. Customer uuid is null.");
        }
        Customer customer = findByUuid(customerUuid);

        customerRepository.delete(customerEntityMapper.mapToEntity(customer));
    }

    @Transactional
    public Customer update(UUID uuid, Customer customer) {
        if (uuid == null) {
            throw new IllegalArgumentException("Updating customer failed. Customer uuid is null.");
        }
        if (customer == null) {
            throw new IllegalArgumentException("Updating customer failed. Customer is null.");
        }

        Customer customerFromDb = findByUuid(uuid);

        Customer updatedCustomer = updateFields(customerFromDb, customer);

        CustomerEntity updatedCustomerEntity = customerRepository
                .save(customerEntityMapper.mapToEntity(updatedCustomer));

        return customerEntityMapper.mapToApiModel(updatedCustomerEntity);
    }

    private Customer updateFields(Customer customerFromDb, Customer newCustomer) {
        if (newCustomer.getUuid() == null) {
            newCustomer.setUuid(customerFromDb.getUuid());
        }
        if (!newCustomer.getUuid().equals(customerFromDb.getUuid())) {
            throw new IllegalArgumentException("Updating customer fields failed. Different uuid's");
        }
        return newCustomer;
    }

}
