package com.sfg.spring6restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sfg.spring6restmvc.model.BeerDTO;
import com.sfg.spring6restmvc.services.BeerService;
import com.sfg.spring6restmvc.services.BeerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;
import java.util.UUID;


@WebMvcTest(BeerController.class)
class BeerControllerTest {


    @Autowired
    MockMvc mockMvc;

    @MockBean
    BeerService beerService;

    @Autowired
    ObjectMapper objectMapper;

    BeerServiceImpl beerServiceImpl;

    @BeforeEach
    void setUp(){
        beerServiceImpl =new BeerServiceImpl();
    }

    @Test
    void testDeleteBeer() throws Exception {
        BeerDTO beer = beerServiceImpl.listBeers(null, null, false, 1, 25).get(0);

        given(beerService.deleteById(any())).willReturn(true);

        mockMvc.perform(delete("/api/v1/beer/" + beer.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(beerService).deleteById(uuidArgumentCaptor.capture());

        assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void testUpdateBeer() throws Exception {
        BeerDTO beer = beerServiceImpl.listBeers(null, null, false, 1, 25).get(0);

        given(beerService.updateBeer(any(UUID.class),any(BeerDTO.class))).willReturn(Optional.ofNullable(beer));

        mockMvc.perform(put("/api/v1/beer/"+beer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isNoContent());

        verify(beerService).updateBeer(any(UUID.class),any(BeerDTO.class));
    }

    @Test
    void testUpdateBeerBlankBeerName() throws Exception {
        BeerDTO beer = beerServiceImpl.listBeers(null, null, false, 1, 25).get(0);
        beer.setBeerName("");

        given(beerService.updateBeer(any(UUID.class),any(BeerDTO.class))).willReturn(Optional.of(beer));

        MvcResult mvcResult = mockMvc.perform(put("/api/v1/beer/" + beer.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(1)))
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());

    }


    @Test
    void testCreateBeerWithNullBeerName() throws Exception {
        BeerDTO build = BeerDTO.builder().build();

        given(beerService.saveNewBeer(any(BeerDTO.class))).willReturn(beerServiceImpl.listBeers(null, null, false, 1, 25).get(1));

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/beer")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(build)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()",is(6))).andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testCreateNewBeer() throws Exception {

        BeerDTO beer = beerServiceImpl.listBeers(null, null, false, 1, 25).get(0);
        beer.setId(null);
        beer.setVersion(null);

        given(beerService.saveNewBeer(any(BeerDTO.class))).willReturn(beerServiceImpl.listBeers(null, null, false, 1, 25).get(1));

        mockMvc.perform(post("/api/v1/beer")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));


    }



    @Test
    void listBeers() throws Exception {

        given(beerService.listBeers(any(), any(), any(), any(), any())).willReturn(beerServiceImpl.listBeers(null, null, false, 1, 25));

        mockMvc.perform(get("/api/v1/beer")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()",is(3)));
    }

    @Test
    void getBeerById() throws Exception {

        BeerDTO testBeer = beerServiceImpl.listBeers(null, null, false, 1, 25).get(0);

        given(beerService.getBeerById(testBeer.getId())).willReturn(Optional.of(testBeer));

        mockMvc.perform(get("/api/v1/beer/"+testBeer.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.id",is(testBeer.getId().toString()+" 0")))
                .andExpect(jsonPath("$.beerName",is(testBeer.getBeerName())));

    }
}