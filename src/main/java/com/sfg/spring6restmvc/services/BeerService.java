package com.sfg.spring6restmvc.services;

import com.sfg.spring6restmvc.model.Beer;

import java.util.List;
import java.util.UUID;

public interface BeerService {
    Beer getBeerById(UUID id);

    List<Beer> listBeers();

    Beer saveNewBeer(Beer beer);

    void updateBeer(UUID id, Beer beer);

    Beer deleteById(UUID id);

    void updateBeerById(UUID id, Beer beer);
}
