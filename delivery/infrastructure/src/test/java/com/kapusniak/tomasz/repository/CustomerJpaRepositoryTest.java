package com.kapusniak.tomasz.repository;

import com.kapusniak.tomasz.entity.CustomerEntity;
import com.kapusniak.tomasz.repository.jpa.CustomerJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "/application-test.properties")
@SqlGroup(
        @Sql(
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
                scripts = {
                        "classpath:h2-scripts/cleanup.sql",
                        "classpath:h2-scripts/insert-data.sql"
                }
        )
)
public class CustomerJpaRepositoryTest {

    @Autowired
    private CustomerJpaRepository customerRepository;

    CustomerEntity prepareCustomerEntity() {
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setEmail("test email");
        customerEntity.setFirstName("test first name");
        customerEntity.setLastName("test last name");
        customerEntity.setEmail("test email");

        return customerEntity;
    }

    @Test
    @DisplayName("should correctly set version number after saving " +
            "or editing an Order entity")
    @Transactional(propagation = NOT_SUPPORTED)
    void versionChecking() {
        // given
        CustomerEntity customerEntity = prepareCustomerEntity();

        // when
        CustomerEntity savedCustomer = customerRepository.save(customerEntity);

        // and
        savedCustomer.setEmail("new email");

        CustomerEntity editedCustomer = customerRepository.save(savedCustomer);

        // then
        assertThat(customerEntity.getVersion()).isEqualTo(0);

        assertThat(savedCustomer.getId()).isNotNull();
        assertThat(savedCustomer.getVersion()).isEqualTo(0);

        assertThat(editedCustomer.getId()).isEqualTo(savedCustomer.getId());
        assertThat(editedCustomer.getVersion()).isEqualTo(savedCustomer.getVersion() + 1);
    }
}