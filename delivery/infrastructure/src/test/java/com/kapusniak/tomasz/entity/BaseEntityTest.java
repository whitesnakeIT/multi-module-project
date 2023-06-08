package com.kapusniak.tomasz.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class BaseEntityTest {

    @Test
    @DisplayName("should check if equals method of uuid is working correctly")
    public void compareWithSameUuid() {
        //given
        UUID uuid = UUID.randomUUID();

        BaseEntity entity1 = new ConcreteEntity();
        BaseEntity entity2 = new ConcreteEntity();

        entity1.setUuid(uuid);
        entity2.setUuid(uuid);

        //when
        boolean equalsResult = entity1.equals(entity2);

        //then
        assertThat(equalsResult).isTrue();
    }

    @Test
    @DisplayName("should check if ConcreteEntity is extending Base entity" +
            " then UUID number is generating correctly")
    public void compareWithDifferentUuid() {

        //given
        BaseEntity entity1 = new ConcreteEntity();
        BaseEntity entity2 = new ConcreteEntity();


        //when
        boolean equalsResult = entity1.equals(entity2);

        //then
        assertThat(equalsResult).isFalse();
    }

    private static class ConcreteEntity extends BaseEntity {
        // This class is needed because BaseEntity is abstract and cannot be instantiated directly
    }
}