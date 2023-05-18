package com.kapusniak.tomasz.adapter.repository;

import com.kapusniak.tomasz.entity.CustomerEntity;
import com.kapusniak.tomasz.mapstruct.CustomerEntityMapper;
import com.kapusniak.tomasz.model.Customer;
import com.kapusniak.tomasz.ports.repository.CustomerRepository;
import com.kapusniak.tomasz.repository.jpa.CustomerJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerRepositoryAdapter implements CustomerRepository {

    private final CustomerJpaRepository customerJpaRepository;

    private final CustomerEntityMapper customerEntityMapper;

    @Override
    public Customer save(Customer customer) {
        CustomerEntity customerEntity = customerEntityMapper.mapToEntity(customer);
        CustomerEntity savedEntity = customerJpaRepository.save(customerEntity);

        return customerEntityMapper
                .mapToModel(savedEntity);
    }

    @Override
    public Customer findById(Long id) {
        Optional<CustomerEntity> customerEntityOptional = customerJpaRepository.findById(id);

        return customerEntityOptional
                .map(customerEntityMapper::mapToModel)
                .orElse(null);
    }

    @Override
    public List<Customer> findAll() {
        return customerJpaRepository
                .findAll()
                .stream()
                .map(customerEntityMapper::mapToModel)
                .toList();
    }

    @Override
    public void delete(Customer customer) {
        CustomerEntity customerEntity = customerEntityMapper.mapToEntity(customer);
        customerJpaRepository.delete(customerEntity);
    }

    @Override
    public Customer update(Customer customer) {
        CustomerEntity customerEntity = customerEntityMapper.mapToEntity(customer);
        CustomerEntity updatedEntity = customerJpaRepository.save(customerEntity);
        return customerEntityMapper.mapToModel(updatedEntity);
    }

}
