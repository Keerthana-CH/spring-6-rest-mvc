package com.sfg.spring6restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sfg.spring6restmvc.model.Customer;
import com.sfg.spring6restmvc.services.CustomerService;
import com.sfg.spring6restmvc.services.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CustomerService customerService;


    @Autowired
    ObjectMapper objectMapper;
    CustomerServiceImpl customerServiceImpl;

    @BeforeEach
    void setUp(){
        customerServiceImpl= new CustomerServiceImpl();
    }

    @Test
    void testCreateNewCustomer() throws Exception {

        Customer customer = customerServiceImpl.listCustomers().get(0);
        customer.setId(null);

        System.out.println(objectMapper.writeValueAsString(customer));

        given(customerService.saveNewCustomer(any(Customer.class))).willReturn(customerServiceImpl.listCustomers().get(1));

        mockMvc.perform(post("/api/v1/customer")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void listCustomers() throws Exception {
        given(customerService.listCustomers()).willReturn(customerServiceImpl.listCustomers());

        mockMvc.perform(get("/api/v1/customer")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()",is(3)));
    }

    @Test
    void getCustomerById() throws Exception {

        Customer customer = customerServiceImpl.listCustomers().get(0);

        given(customerService.getCustomerById(100L)).willReturn(customer);

        mockMvc.perform(get("/api/v1/customer/"+"100")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.id",is(customer.getId().toString())))
                .andExpect(jsonPath("$.customerName",is(customer.getCustomerName())));
    }
}