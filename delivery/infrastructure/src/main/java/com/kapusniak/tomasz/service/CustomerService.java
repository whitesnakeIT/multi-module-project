package com.kapusniak.tomasz.service;

import com.kapusniak.tomasz.entity.CustomerEntity;
import com.kapusniak.tomasz.entity.OrderEntity;
import com.kapusniak.tomasz.mapper.CustomerEntityMapper;
import com.kapusniak.tomasz.openapi.model.Customer;
import com.kapusniak.tomasz.repository.jpa.CustomerJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.kapusniak.tomasz.service.PagingService.PAGE_SIZE;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerService {

    private final CustomerJpaRepository customerRepository;

    private final CustomerEntityMapper customerEntityMapper;

    private final PagingService pagingService;

    @Transactional
    @CachePut(value = "customers", key = "#customer.uuid")
    public Customer save(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Saving customer failed. Customer is null.");
        }
        CustomerEntity customerEntity = customerEntityMapper.mapToEntity(customer);
        CustomerEntity savedEntity = customerRepository.save(customerEntity);

        return customerEntityMapper.mapToApiModel(savedEntity);
    }

    @Cacheable(value = "customers")
    public List<Customer> findAll(Integer page) {
        Integer pageNumber = pagingService.validatePageNumber(page);

        return customerRepository
                .findAll(PageRequest.of(pageNumber, PAGE_SIZE))
                .stream()
                .map(customerEntityMapper::mapToApiModel)
                .toList();
    }

    @Cacheable(value = "customer", key = "#customerUuid")
    public Customer findByUuid(UUID customerUuid) {
        if (customerUuid == null) {
            throw new EntityNotFoundException("Searching for customer failed. Customer uuid is null.");
        }
        return customerEntityMapper.mapToApiModel(customerRepository.findByUuid(customerUuid)
                .orElseThrow(() ->
                        new EntityNotFoundException("Searching for customer failed. Unrecognized uuid " + customerUuid)));
    }

    @Transactional
    @CacheEvict(value = "customer", key = "#customerUuid")
    public void delete(UUID customerUuid) {
        if (customerUuid == null) {
            throw new IllegalArgumentException("Deleting customer failed. Customer uuid is null.");
        }
        Customer customer = findByUuid(customerUuid);

        customerRepository.delete(customerEntityMapper.mapToEntity(customer));
    }

    @Transactional
    @CachePut(value = "customers", key = "#uuid")
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

    public List<CustomerEntity> findAllEntitiesByIdIn(List<Long> customerIds, Integer page) {
        Integer pageNumber = pagingService.validatePageNumber(page);
        List<CustomerEntity> customerEntities = customerRepository
//                .findAllEntitiesByIdIn(customerIds, PageRequest.of(pageNumber, PAGE_SIZE));
                .findAllPagedByIdIn(customerIds, PageRequest.of(pageNumber, PAGE_SIZE))
                .getContent();

        return customerEntities;
    }

    public CustomerEntity extractCustomersEntities(List<CustomerEntity> customerEntities, OrderEntity orderEntity) {
        if (customerEntities == null) {
            throw new RuntimeException();
        }
        if (orderEntity == null) {
            throw new RuntimeException();
        }

        return customerEntities.stream()
                .filter(customerEntity -> customerEntity.getOrders().contains(orderEntity)) // tutaj gdy daje getOrders() to robia sie te n+1
                .findFirst()                                                                         // ale inaczej nie moge przypisac do orderu customera zeby bylo
                .orElseThrow(RuntimeException::new);                                                                      // klauzula in, no chyba ze nie ogarniam juz tego :)
    }
}
