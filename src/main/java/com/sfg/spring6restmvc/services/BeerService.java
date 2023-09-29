package com.sfg.spring6restmvc.services;

import com.sfg.spring6restmvc.model.BeerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {
    Optional<BeerDTO> getBeerById(UUID id);

    List<BeerDTO> listBeers();

    BeerDTO saveNewBeer(BeerDTO beer);

    Optional<BeerDTO> updateBeer(UUID id, BeerDTO beer);

    Boolean deleteById(UUID id);

    void updateBeerById(UUID id, BeerDTO beer);
}
