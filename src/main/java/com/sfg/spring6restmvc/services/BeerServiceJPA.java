package com.sfg.spring6restmvc.services;

import com.sfg.spring6restmvc.entities.Beer;
import com.sfg.spring6restmvc.mappers.BeerMapper;
import com.sfg.spring6restmvc.model.BeerDTO;
import com.sfg.spring6restmvc.model.BeerStyle;
import com.sfg.spring6restmvc.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;


@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    private static final Integer DEFAULT_PAGE_NUMBER = 1;
    private static final Integer DEFAULT_PAGE_SIZE = 25;

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        return Optional.ofNullable(beerMapper.beerTobeerDto(beerRepository.findById(id)
                .orElse(null)));
    }

    @Override
    public Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber, Integer pageSize) {

        PageRequest pageRequest = buildPageRequest(pageNumber,pageSize);

        Page<Beer> beerPage;
        if (StringUtils.hasText(beerName) && beerStyle==null){
            beerPage = listBeersByBeerName(beerName, pageRequest);
        } else if (!StringUtils.hasText(beerName) && beerStyle!=null) {
            beerPage = listBeersByBeerStyle(beerStyle, pageRequest);
        } else if (StringUtils.hasText(beerName) && beerStyle!=null) {
            beerPage = listBeersByBeerNameAndBeerStyle(beerName,beerStyle, pageRequest);
        }else {
            beerPage = beerRepository.findAll(pageRequest);
        }

        if (showInventory!=null && !showInventory){
            beerPage.forEach(beer -> beer.setQuantityOnHand(null));
        }

        return beerPage.map(beerMapper::beerTobeerDto);
    }

    public PageRequest buildPageRequest(Integer pageNumber, Integer pageSize){
        int queryPageNumber;
        int queryPageSize;

        if (pageNumber!=null && pageNumber > 0){
            queryPageNumber = pageNumber - 1;
        }else {
            queryPageNumber = DEFAULT_PAGE_NUMBER;
        }
        if (pageSize==null){
            queryPageSize = DEFAULT_PAGE_SIZE;
        }else if (pageSize > 1000) {
            queryPageSize = 1000;
        }else queryPageSize = pageSize;

        Sort sort = Sort.by(Sort.Order.asc("beerStyle"));

        return PageRequest.of(queryPageNumber,queryPageSize,sort);
    }

    private Page<Beer> listBeersByBeerNameAndBeerStyle(String beerName, BeerStyle beerStyle, Pageable pageable) {
        return beerRepository.findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle("%" + beerName + "%",beerStyle, pageable);
    }

    public Page<Beer> listBeersByBeerStyle(BeerStyle beerStyle, Pageable pageable) {
        return beerRepository.findAllByBeerStyle(beerStyle, pageable);
    }

    public Page<Beer> listBeersByBeerName(String beerName, Pageable pageable) {
        return beerRepository.findAllByBeerNameIsLikeIgnoreCase("%" + beerName + "%", pageable);
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
