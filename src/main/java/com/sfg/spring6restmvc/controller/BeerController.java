package com.sfg.spring6restmvc.controller;

import com.sfg.spring6restmvc.model.BeerDTO;
import com.sfg.spring6restmvc.services.BeerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/beer")
public class BeerController {
    private final BeerService beerService;

    @RequestMapping(method = RequestMethod.GET)
    public List<BeerDTO> listBeers(){
        return beerService.listBeers();
    }

    @RequestMapping(value ="/{beerId}" , method = RequestMethod.GET)
    public BeerDTO getBeerById(@PathVariable("beerId") UUID beerId){

        log.debug("getBeerById - ********* controller BeerController"+beerId);
        return beerService.getBeerById(beerId).orElseThrow(NotFoundException::new);
    }

    @PostMapping()
    public ResponseEntity<Object> saveNewBeer(@RequestBody BeerDTO beer){
        BeerDTO savedBeer = beerService.saveNewBeer(beer);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location","/api/v1/beer/"+savedBeer.getId());

        return new ResponseEntity<>(headers,HttpStatus.CREATED);
    }

    @PutMapping("/{beerId}")
    public ResponseEntity<Object> updateBeer(@PathVariable("beerId") UUID id,@RequestBody BeerDTO beer){
        beerService.updateBeer(id,beer);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @DeleteMapping("{beerId}")
    public ResponseEntity<Object> deleteById(@PathVariable("beerId") UUID id){
        BeerDTO beer = beerService.deleteById(id);
        if(beer==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("{beerId}")
    public ResponseEntity<Object> updateBeerById(@PathVariable("beerId") UUID id,@RequestBody BeerDTO beer){
        beerService.updateBeerById(id,beer);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
