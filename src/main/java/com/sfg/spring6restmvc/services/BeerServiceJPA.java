package com.sfg.spring6restmvc.services;

import com.sfg.spring6restmvc.entities.Beer;
import com.sfg.spring6restmvc.mappers.BeerMapper;
import com.sfg.spring6restmvc.model.BeerDTO;
import com.sfg.spring6restmvc.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;
    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        return Optional.ofNullable(beerMapper.beerTobeerDto(beerRepository.findById(id)
                .orElse(null)));
    }

    @Override
    public List<BeerDTO> listBeers() {
        return beerRepository.findAll()
                .stream()
                .map(beerMapper::beerTobeerDto)
                .collect(Collectors.toList());
    }

    @Override
    public BeerDTO saveNewBeer(BeerDTO beer) {

        return beerMapper.beerTobeerDto(beerRepository.save(beerMapper.beerDtoToBeer(beer)));
    }

    @Override
    public void updateBeer(UUID id, BeerDTO beer) {
        beerRepository.findById(id).ifPresent(founfBeer ->{
            founfBeer.setBeerName(beer.getBeerName());
            founfBeer.setBeerStyle(beer.getBeerStyle());
            founfBeer.setUpc(beer.getUpc());
            founfBeer.setPrice(beer.getPrice());
            founfBeer.setQuantityOnHand(beer.getQuantityOnHand());
            founfBeer.setUpdateDate(beer.getUpdateDate());
            founfBeer.setCreatedDate(beer.getCreatedDate());

            beerRepository.save(founfBeer);
        });
    }

    @Override
    public BeerDTO deleteById(UUID id) {
        return null;
    }

    @Override
    public void updateBeerById(UUID id, BeerDTO beer) {

    }
}
