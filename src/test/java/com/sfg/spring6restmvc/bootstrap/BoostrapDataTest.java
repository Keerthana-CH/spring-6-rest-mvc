package com.sfg.spring6restmvc.bootstrap;

import com.sfg.spring6restmvc.repositories.BeerRepository;
import com.sfg.spring6restmvc.repositories.CustomerRepository;
import com.sfg.spring6restmvc.services.BeerCsvService;
import com.sfg.spring6restmvc.services.BeerCsvServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(BeerCsvServiceImpl.class)
class BoostrapDataTest {

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BeerCsvService beerCsvService;

    BoostrapData boostrapData;


    @BeforeEach
    void setUp() {
        boostrapData = new BoostrapData(beerRepository,customerRepository,beerCsvService);
    }

    @Test
    void run() throws Exception {
        boostrapData.run(null);

        assertThat(beerRepository.count()).isEqualTo(2413);
        assertThat(customerRepository.count()).isEqualTo(3);
    }
}