package com.kapusniak.tomasz.repository;

import com.kapusniak.tomasz.entity.CourierEntity;
import com.kapusniak.tomasz.openapi.model.CourierCompany;
import com.kapusniak.tomasz.repository.jpa.CourierJpaRepository;
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
public class CourierJpaRepositoryTest {

    @Autowired
    private CourierJpaRepository courierRepository;

    CourierEntity prepareCourierEntity() {
        CourierEntity courierEntity = new CourierEntity();
        courierEntity.setFirstName("test first name");
        courierEntity.setLastName("test last name");
        courierEntity.setCourierCompany(CourierCompany.DHL);
        return courierEntity;
    }

    @Test
    @DisplayName("should correctly set version number after saving " +
            "or editing an Order entity")
    @Transactional(propagation = NOT_SUPPORTED)
    void versionChecking() {
        // given
        CourierEntity courierEntity = prepareCourierEntity();

        // when
        CourierEntity savedCourier = courierRepository.save(courierEntity);

        // and
        savedCourier.setFirstName("new first name");

        CourierEntity editedCourier = courierRepository.save(savedCourier);

        // then
        assertThat(courierEntity.getVersion()).isEqualTo(0);

        assertThat(savedCourier.getId()).isNotNull();
        assertThat(savedCourier.getVersion()).isEqualTo(0);

        assertThat(editedCourier.getId()).isEqualTo(savedCourier.getId());
        assertThat(editedCourier.getVersion()).isEqualTo(savedCourier.getVersion() + 1);
    }
}