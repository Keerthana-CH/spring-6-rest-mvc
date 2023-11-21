package com.sfg.spring6restmvc.controller;

import com.sfg.spring6restmvc.model.BeerDTO;
import com.sfg.spring6restmvc.model.BeerStyle;
import com.sfg.spring6restmvc.services.BeerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/beer")
public class BeerController {
    private final BeerService beerService;

    public static final String BEER_PATH = "/api/v1/beer";
    public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";

    @RequestMapping(method = RequestMethod.GET)
    public List<BeerDTO> listBeers(@RequestParam(required = false) String beerName,
                                   @RequestParam(required = false) BeerStyle beerStyle, Boolean showInventory){
        return beerService.listBeers(beerName, beerStyle, showInventory);
    }

    @RequestMapping(value ="/{beerId}" , method = RequestMethod.GET)
    public BeerDTO getBeerById(@PathVariable("beerId") UUID beerId){

        log.debug("getBeerById - ********* controller BeerController"+beerId);
        return beerService.getBeerById(beerId).orElseThrow(NotFoundException::new);
    }

    @PostMapping()
    public ResponseEntity<Object> saveNewBeer(@Validated @RequestBody BeerDTO beer){
        BeerDTO savedBeer = beerService.saveNewBeer(beer);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location","/api/v1/beer/"+savedBeer.getId());

        return new ResponseEntity<>(headers,HttpStatus.CREATED);
    }

    @PutMapping("/{beerId}")
    public ResponseEntity<Object> updateBeer(@PathVariable("beerId") UUID id,@Validated @RequestBody BeerDTO beer){
        Optional<BeerDTO> beerDTO = beerService.updateBeer(id, beer);
        return beerDTO.map(beerDTO1 -> new ResponseEntity<>(HttpStatus.NO_CONTENT)).orElseThrow(NotFoundException::new);

    }

    @DeleteMapping("{beerId}")
    public ResponseEntity<Object> deleteById(@PathVariable("beerId") UUID id){
        Boolean b = beerService.deleteById(id);
        if(!b){
            throw new NotFoundException();
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("{beerId}")
    public ResponseEntity<Object> updateBeerById(@PathVariable("beerId") UUID id,@RequestBody BeerDTO beer){
        beerService.patchBeerById(id,beer);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
