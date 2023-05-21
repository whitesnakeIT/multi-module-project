package com.kapusniak.tomasz.service;

import com.kapusniak.tomasz.entity.CustomerEntity;
import com.kapusniak.tomasz.mapstruct.CustomerEntityMapper;
import com.kapusniak.tomasz.openapi.model.Customer;
import com.kapusniak.tomasz.repository.jpa.CustomerJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class CustomerServiceTest {

    Customer customer = new Customer();
    CustomerEntity customerEntity = new CustomerEntity();

    List<Customer> customerList = new ArrayList<>();
    List<CustomerEntity> customerEntityList = new ArrayList<>();

    @Mock
    private CustomerJpaRepository customerRepository;

    @Mock
    private CustomerEntityMapper customerEntityMapper;
    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void setup() {
        customer.setId(1L);
        customer.setEmail("testEmail");
        customer.setFirstName("testFirstName");
        customer.setLastName("testLastName");

        customerList.add(customer);
        customerList.add(customer);

        customerEntity.setId(1L);
        customerEntity.setEmail("testEmail");
        customerEntity.setFirstName("testFirstName");
        customerEntity.setLastName("testLastName");

        customerEntityList.add(customerEntity);
        customerEntityList.add(customerEntity);

        when(customerEntityMapper
                .mapToEntity(any(Customer.class)))
                .thenReturn(customerEntity);
        when(customerEntityMapper
                .mapToApiModel(any(CustomerEntity.class)))
                .thenReturn(customer);
    }

    @Test
    @DisplayName("should correctly save an Customer entity exactly once")
    void save() {

        //given
        when(customerRepository
                .save(any(CustomerEntity.class)))
                .thenReturn(customerEntity);

        // when
        Customer result = customerService.save(customer);

        // then
        then(customerRepository)
                .should(times(1))
                .save(customerEntityMapper.mapToEntity(customer));

        // verify
        assertThat(customer).isEqualTo(result);
    }

    @Test
    @DisplayName("should throw an exception when Customer entity is null and check method invocations")
    void saveNull() {

        // given
        customer = null;

        // when
        Throwable throwable = catchThrowable(() ->
                customerService.save(customer));

        // then
        assertThat(throwable)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Saving customer failed. Customer is null.");

        // verify
        then(customerRepository).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("should return list of all customer with correct size and check method invocations")
    void findAll() {

        // given
        given(customerRepository.findAll())
                .willReturn(customerEntityList);

        // when
        List<Customer> allCustomers = customerService.findAll();

        // then
        assertThat(allCustomers.size())
                .isEqualTo(2);

        // verify
        then(customerRepository)
                .should(times(1))
                .findAll();
    }

    @Test
    @DisplayName("should return customer based on customer id")
    void findById() {

        // given
        given(customerRepository.findById(
                anyLong()))
                .willReturn(Optional.of(customerEntity));
        Long customerId = 1L;

        // when
        Customer customerById = customerService.findById(customerId);

        // then
        assertThat(customerById.getId())
                .isNotNull();
    }

    @Test
    @DisplayName("should throw an exception when customer id is null")
    void findByIdNull() {

        // given
        Long customerId = null;

        // when
        Throwable throwable = catchThrowable(() ->
                customerService.findById(customerId));

        // then
        assertThat(throwable)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Searching for customer failed. Customer id is null.");

        // verify
        then(customerRepository)
                .shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("should delete an customer based on customer id")
    void delete() {

        // given
        given(customerRepository.findById(
                anyLong()))
                .willReturn(Optional.of(customerEntity));
        Long customerId = 1L;

        // when
        customerService.delete(customerId);

        // then
        then(customerRepository)
                .should(times(1))
                .delete(customerEntityMapper.mapToEntity(customer));
    }

    @Test
    @DisplayName("should throw an exception when customer id is null")
    void deleteNull() {
        // given
        Long customerId = null;

        // when
        Throwable throwable = catchThrowable(() ->
                customerService.delete(customerId));

        // then
        assertThat(throwable)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Deleting customer failed. Customer id is null.");
    }
}