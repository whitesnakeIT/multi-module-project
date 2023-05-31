package com.kapusniak.tomasz.service;

import com.kapusniak.tomasz.entity.CustomerEntity;
import com.kapusniak.tomasz.entity.OrderEntity;
import com.kapusniak.tomasz.mapstruct.CustomerEntityMapper;
import com.kapusniak.tomasz.openapi.model.Customer;
import com.kapusniak.tomasz.openapi.model.Order;
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

    @Test
    @DisplayName("should throw an exception when id is null")
    void updateNullId() {
        // given
        Long customerId = null;

        // when
        Throwable throwable = catchThrowable(() ->
                customerService.update(customerId, customer));

        // then
        assertThat(throwable)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Updating customer failed. Customer id is null.");
    }

    @Test
    @DisplayName("should throw an exception when customer is null")
    void updateNullCustomer() {
        // given
        Long customerId = 1L;
        Customer customer = null;

        // when
        Throwable thrown = catchThrowable(() ->
                customerService.update(customerId, customer));

        // then
        assertThat(thrown)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Updating customer failed. Customer is null.");
    }

    @Test
    @DisplayName("should throw an exception when newCustomer's id doesn't match customerFromDb's id")
    void updateIdMissMatch() {
        // given
        Customer newCustomer = new Customer();
        Long oldId = 1L;
        Long newId = 2L;
        newCustomer.setId(newId);

        // and
        when(customerRepository.findById(anyLong()))
                .thenReturn(Optional.of(customerEntity));
        // when
        Throwable throwable = catchThrowable(() ->
                customerService.update(oldId, newCustomer));

        // then
        assertThat(throwable)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Updating customer fields failed. Different id's");
    }

    @Test
    @DisplayName("should correctly update customer when valid id and customer are provided")
    void shouldUpdateCustomer() {
        // given
        Long customerId = 1L;
        Customer changedCustomer = prepareCustomerToEdit();
        CustomerEntity changedCustomerEntity = prepareCustomerEntityToEdit();

        // and
        when(customerRepository.findById(anyLong()))
                .thenReturn(Optional.of(customerEntity));
        when(customerRepository.save(any(CustomerEntity.class)))
                .thenReturn(changedCustomerEntity);

        //and
        when(customerEntityMapper
                .mapToApiModel(changedCustomerEntity))
                .thenReturn(changedCustomer);

        // when
        Customer updatedCustomer = customerService.update(customerId, changedCustomer);

        // then
        assertThat(updatedCustomer).isNotNull();
        assertThat(updatedCustomer.getId()).isEqualTo(changedCustomer.getId());
        assertThat(updatedCustomer.getFirstName()).isEqualTo(changedCustomer.getFirstName());
        assertThat(updatedCustomer.getLastName()).isEqualTo(changedCustomer.getLastName());
        assertThat(updatedCustomer.getEmail()).isEqualTo(changedCustomer.getEmail());

        assertThat(updatedCustomer.getOrders().get(1).getId()).isEqualTo(changedCustomer.getOrders().get(1).getId());
        assertThat(updatedCustomer.getOrders().get(1).getId()).isEqualTo(changedCustomer.getOrders().get(1).getId());

        // verify
        then(customerRepository)
                .should(times(1))
                .save(customerEntity);
    }

    private Customer prepareCustomerToEdit() {
        Long customerId = 1L;
        String newFirstName = "newFirstName";
        String newLastName = "newLastName";
        String newEmail = "newEmail";
        Long newOrder1Id = 3L;
        Long newOrder2Id = 3L;

        Customer changedCustomer = new Customer();
        changedCustomer.setId(customerId);
        changedCustomer.setFirstName(newFirstName);
        changedCustomer.setLastName(newLastName);
        changedCustomer.setEmail(newEmail);

        Order newOrder1 = new Order();
        newOrder1.setId(newOrder1Id);
        Order newOrder2 = new Order();
        newOrder2.setId(newOrder2Id);

        changedCustomer.setOrders(List.of(newOrder1, newOrder2));

        return changedCustomer;
    }

    private CustomerEntity prepareCustomerEntityToEdit() {
        Long customerId = 1L;
        String newFirstName = "newFirstName";
        String newLastName = "newLastName";
        String newEmail = "newEmail";
        Long newOrder1Id = 3L;
        Long newOrder2Id = 3L;

        CustomerEntity changedCustomerEntity = new CustomerEntity();
        changedCustomerEntity.setId(customerId);
        changedCustomerEntity.setFirstName(newFirstName);
        changedCustomerEntity.setLastName(newLastName);
        changedCustomerEntity.setEmail(newEmail);
        OrderEntity newOrderEntity1 = new OrderEntity();
        newOrderEntity1.setId(newOrder1Id);
        OrderEntity newOrderEntity2 = new OrderEntity();
        newOrderEntity2.setId(newOrder2Id);

        changedCustomerEntity.setOrders(List.of(newOrderEntity1, newOrderEntity2));

        return changedCustomerEntity;
    }
}