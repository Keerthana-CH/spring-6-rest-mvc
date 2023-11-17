package com.sfg.spring6restmvc.services;

import com.sfg.spring6restmvc.entities.Beer;
import com.sfg.spring6restmvc.mappers.BeerMapper;
import com.sfg.spring6restmvc.model.BeerDTO;
import com.sfg.spring6restmvc.model.BeerStyle;
import com.sfg.spring6restmvc.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
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
    public List<BeerDTO> listBeers(String beerName, BeerStyle beerStyle) {

        List<Beer> beerList;
        if (StringUtils.hasText(beerName) && beerStyle==null){
            beerList = listBeersByBeerName(beerName);
        } else if (!StringUtils.hasText(beerName) && beerStyle!=null) {
            beerList = listBeersByBeerStyle(beerStyle);
        } else {
            beerList = beerRepository.findAll();
        }
        return beerList
                .stream()
                .map(beerMapper::beerTobeerDto)
                .collect(Collectors.toList());
    }

    public List<Beer> listBeersByBeerStyle(BeerStyle beerStyle) {
        return beerRepository.findAllByBeerStyle(beerStyle);
    }

    public List<Beer> listBeersByBeerName(String beerName) {
        return beerRepository.findAllByBeerNameIsLikeIgnoreCase("%" + beerName + "%");
    }

    @Override
    public BeerDTO saveNewBeer(BeerDTO beer) {

        return beerMapper.beerTobeerDto(beerRepository.save(beerMapper.beerDtoToBeer(beer)));
    }

    @Override
    public Optional<BeerDTO> updateBeer(UUID id, BeerDTO beer) {

        AtomicReference<BeerDTO> atomicReference = new AtomicReference<>();

        beerRepository.findById(id).ifPresent(founfBeer ->{
            founfBeer.setBeerName(beer.getBeerName());
            founfBeer.setBeerStyle(beer.getBeerStyle());
            founfBeer.setUpc(beer.getUpc());
            founfBeer.setPrice(beer.getPrice());
            founfBeer.setQuantityOnHand(beer.getQuantityOnHand());
            founfBeer.setUpdateDate(beer.getUpdateDate());
            founfBeer.setCreatedDate(beer.getCreatedDate());

            atomicReference.set(beerMapper.beerTobeerDto(beerRepository.save(founfBeer)));

        });
        return Optional.ofNullable(atomicReference.get());
    }

    @Override
    public Boolean deleteById(UUID id) {
        if (beerRepository.existsById(id)){
            beerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Optional<BeerDTO> patchBeerById(UUID beerId, BeerDTO beer) {
        AtomicReference<Optional<BeerDTO>> atomicReference = new AtomicReference<>();

        beerRepository.findById(beerId).ifPresentOrElse(foundBeer -> {
            if (StringUtils.hasText(beer.getBeerName())){
                foundBeer.setBeerName(beer.getBeerName());
            }
            if (beer.getBeerStyle() != null){
                foundBeer.setBeerStyle(beer.getBeerStyle());
            }
            if (StringUtils.hasText(beer.getUpc())){
                foundBeer.setUpc(beer.getUpc());
            }
            if (beer.getPrice() != null){
                foundBeer.setPrice(beer.getPrice());
            }
            if (beer.getQuantityOnHand() != null){
                foundBeer.setQuantityOnHand(beer.getQuantityOnHand());
            }
            atomicReference.set(Optional.of(beerMapper
                    .beerTobeerDto(beerRepository.save(foundBeer))));
        }, () -> atomicReference.set(Optional.empty()));

        return atomicReference.get();
    }
}
