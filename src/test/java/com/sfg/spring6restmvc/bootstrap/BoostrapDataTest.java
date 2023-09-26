package com.sfg.spring6restmvc.bootstrap;

import com.sfg.spring6restmvc.repositories.BeerRepository;
import com.sfg.spring6restmvc.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BoostrapDataTest {

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    CustomerRepository customerRepository;

    BoostrapData boostrapData;


    @BeforeEach
    void setUp() {
        boostrapData = new BoostrapData(beerRepository,customerRepository);
    }

    @Test
    void run() throws Exception {
        boostrapData.run(null);

        assertThat(beerRepository.count()).isEqualTo(3);
        assertThat(customerRepository.count()).isEqualTo(3);
    }
}