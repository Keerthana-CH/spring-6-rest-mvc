package com.sfg.spring6restmvc.repositories;

import com.sfg.spring6restmvc.entities.Beer;
import com.sfg.spring6restmvc.model.BeerStyle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testSaveBeer() {
        Beer beer = beerRepository.save(Beer.builder()
                        .beerName("New Beer 2333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333")
                        .beerStyle(BeerStyle.IPA)
                        .upc("12121212121")
                        .price(new BigDecimal("122.2"))
                .build());
        beerRepository.flush();

        assertThat(beer).isNotNull();
        assertThat(beer.getId()).isNotNull();
    }
}