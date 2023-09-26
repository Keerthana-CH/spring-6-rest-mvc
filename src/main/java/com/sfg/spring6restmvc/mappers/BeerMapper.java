package com.sfg.spring6restmvc.mappers;

import com.sfg.spring6restmvc.entities.Beer;
import com.sfg.spring6restmvc.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {

    Beer beerDtoToBeer(BeerDTO beerDTO);
    BeerDTO beerTobeerDto(Beer beer);
}
