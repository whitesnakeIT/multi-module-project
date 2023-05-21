//package com.kapusniak.tomasz.adapter.service;
//
//import com.kapusniak.tomasz.adapter.repository.CustomerRepositoryAdapter;
//import com.kapusniak.tomasz.model.Customer;
//import com.kapusniak.tomasz.ports.service.CustomerService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class CustomerServiceAdapter implements CustomerService {
//
//    private final CustomerRepositoryAdapter customerRepository;
//
//    @Override
//    public Customer save(Customer customer) {
//        if (customer == null) {
//            throw new RuntimeException("Saving customer failed. Customer is null.");
//        }
//
//        return customerRepository.save(customer);
//    }
//
//    @Override
//    public Customer findById(Long customerId) {
//        if (customerId == null) {
//            throw new RuntimeException("Searching for customer failed. Customer id is null.");
//        }
//
//        return customerRepository.findById(customerId);
//    }
//
//    @Override
//    public List<Customer> findAll() {
//        return customerRepository.findAll();
//    }
//
//    @Override
//    public void delete(Long customerId) {
//        if (customerId == null) {
//            throw new RuntimeException("Deleting customer failed. Customer id is null.");
//        }
//        Customer customer = findById(customerId);
//        customerRepository.delete(customer);
//    }
//
//    @Override
//    public Customer update(Customer customer) {
//        return null;
//    }
//}
