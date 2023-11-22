package com.sfg.spring6restmvc.repositories;

import com.sfg.spring6restmvc.bootstrap.BoostrapData;
import com.sfg.spring6restmvc.entities.Beer;
import com.sfg.spring6restmvc.model.BeerStyle;
import com.sfg.spring6restmvc.services.BeerCsvServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({BoostrapData.class, BeerCsvServiceImpl.class})
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testGetBeerListByName(){
        Page<Beer> allByBeerNameIsLikeIgnoreCase = beerRepository.findAllByBeerNameIsLikeIgnoreCase("%IPA", null);

        assertThat(allByBeerNameIsLikeIgnoreCase.getContent().size()).isEqualTo(291);
    }

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