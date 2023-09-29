package com.sfg.spring6restmvc.controller;

import com.sfg.spring6restmvc.entities.Customer;
import com.sfg.spring6restmvc.model.CustomerDTO;
import com.sfg.spring6restmvc.repositories.CustomerRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerControllerIT {

    @Autowired
    CustomerController customerController;

    @Autowired
    CustomerRepository customerRepository;


    @Transactional
    @Rollback
    @Test
    void testEmptyList() {
        customerRepository.deleteAll();
        List<Customer> all = customerRepository.findAll();
        assertThat(all.size()).isEqualTo(0);
    }

    @Test
    void listCustomers() {
        List<Customer> all = customerRepository.findAll();

        assertThat(all.size()).isEqualTo(3);
    }

    @Test
    void testCustomerByIdNotFound() {
        assertThrows(NotFoundException.class,()->{
            customerController.getCustomerById(UUID.randomUUID());
        });
    }

    @Test
    void testGetById() {
        Customer customer = customerRepository.findAll().get(0);

        CustomerDTO customerById = customerController.getCustomerById(customer.getId());

        assertThat(customerById).isNotNull();

    }
}