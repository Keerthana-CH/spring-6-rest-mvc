package com.sfg.spring6restmvc.services;

import com.sfg.spring6restmvc.model.BeerDTO;

import java.util.List;
import java.util.UUID;

public interface BeerService {
    BeerDTO getBeerById(UUID id);

    List<BeerDTO> listBeers();

    BeerDTO saveNewBeer(BeerDTO beer);

    void updateBeer(UUID id, BeerDTO beer);

    BeerDTO deleteById(UUID id);

    void updateBeerById(UUID id, BeerDTO beer);
}
