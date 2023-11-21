package com.sfg.spring6restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sfg.spring6restmvc.entities.Beer;
import com.sfg.spring6restmvc.mappers.BeerMapper;
import com.sfg.spring6restmvc.model.BeerDTO;
import com.sfg.spring6restmvc.model.BeerStyle;
import com.sfg.spring6restmvc.repositories.BeerRepository;
import jakarta.transaction.Transactional;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class BeerControllerIT {

    @Autowired
    BeerController beerController;

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    BeerMapper beerMapper;

    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext wac;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }


    @Test
    void tesListBeersByStyleAndNameShowInventoryTruePage2() throws Exception {
        mockMvc.perform(get(BeerController.BEER_PATH)
                        .queryParam("beerName", "IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("showInventory", "true")
                        .queryParam("pageNumber", "2")
                        .queryParam("pageSize", "50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(50)))
                .andExpect(jsonPath("$.[0].quantityOnHand").value(IsNull.notNullValue()));
    }
    @Test
    void testListBeersByBeerNameAndBeerStyleAndShowInventoryFalse() throws Exception {
        mockMvc.perform(get(BeerController.BEER_PATH)
                        .queryParam("beerName","IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("showInventory","false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].quantityOnHand").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.size()",is(310)));
    }

    @Test
    void testListBeersByBeerNameAndBeerStyleAndShowInventoryTrue() throws Exception {
        mockMvc.perform(get(BeerController.BEER_PATH)
                        .queryParam("beerName","IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("showInventory","true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].quantityOnHand").value(IsNull.notNullValue()))
                .andExpect(jsonPath("$.size()",is(310)));
    }
    @Test
    void testListBeersByBeerNameAndBeerStyle() throws Exception {
        mockMvc.perform(get(BeerController.BEER_PATH)
                        .queryParam("beerName","IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()",is(310)));
    }
    @Test
    void testListBeersByBeerStyle() throws Exception {
        mockMvc.perform(get(BeerController.BEER_PATH)
                        .queryParam("beerStyle", BeerStyle.IPA.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()",is(548)));
    }

    @Test
    void testListBeersByBeerName() throws Exception {
        mockMvc.perform(get(BeerController.BEER_PATH)
                .queryParam("beerName","IPA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()",is(336)));
    }
    @Test
    void testPatchBeerNameTooLong() throws Exception {
        Beer beer = beerRepository.findAll().get(0);

        Map<String, Object> beerMap = new HashMap<>();
        beerMap.put("beerName", "New Name 221212112121212222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222");

        MvcResult mvcResult = mockMvc.perform(patch(BeerController.BEER_PATH_ID, beer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerMap)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()",is(1)))
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());

    }

    @Test
    void testDeleteBeerNotFound() {
        assertThrows(NotFoundException.class, () ->{
            beerController.deleteById(UUID.randomUUID());
        });
    }

    @Transactional
    @Rollback
    @Test
    void testDeleteBeer() {
        Beer beer = beerRepository.findAll().get(0);

        ResponseEntity<Object> responseEntity = beerController.deleteById(beer.getId());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(beerRepository.findById(beer.getId())).isEmpty();
    }

    @Test
    void testUpdateNotFound() {
        assertThrows(NotFoundException.class,() ->{
            beerController.updateBeer(UUID.randomUUID(),BeerDTO.builder().build());
        });
    }

    @Rollback
    @Transactional
    @Test
    void testUpdateBeer() {
        Beer beer = beerRepository.findAll().get(0);
        BeerDTO beerDTO = beerMapper.beerTobeerDto(beer);
        beerDTO.setId(null);
        beerDTO.setVersion(null);
        final String beerName ="UPDATED";
        beerDTO.setBeerName(beerName);

        ResponseEntity<Object> responseEntity = beerController.updateBeer(beer.getId(), beerDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        Beer byId = beerRepository.findById(beer.getId()).get();
        assertThat(byId.getBeerName()).isEqualTo(beerName);
    }


    @Transactional
    @Rollback
    @Test
    void testSaveBeer() {
        BeerDTO newBeer = BeerDTO.builder()
                .beerName("New Beer")
                .build();
        ResponseEntity<Object> responseEntity = beerController.saveNewBeer(newBeer);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] location = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID uuid = UUID.fromString(location[4]);
        Beer beer = beerRepository.getReferenceById(uuid);
        assertThat(beer).isNotNull();


    }

    @Test
    void testBeerIdNotFoundException() {
        assertThrows(NotFoundException.class,() ->{
            beerController.getBeerById(UUID.randomUUID());
        });
    }

    @Test
    void testGetById() {
        Beer beer = beerRepository.findAll().get(0);

        BeerDTO beerDTO = beerController.getBeerById(beer.getId());

        assertThat(beerDTO).isNotNull();
    }

    @Test
    void testListBeers() {
        List<BeerDTO> beerDTOS = beerController.listBeers(null,null, false, 1, 25);

        assertThat(beerDTOS.size()).isEqualTo(2413);
    }

    @Rollback
    @Transactional
    @Test
    void testEmptyList() {
        beerRepository.deleteAll();
        List<BeerDTO> beerDTOS = beerController.listBeers(null,null, false, 1, 25);

        assertThat(beerDTOS.size()).isEqualTo(0);
    }
}