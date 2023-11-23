package com.sfg.spring6restmvc.controller;

import com.sfg.spring6restmvc.entities.Customer;
import com.sfg.spring6restmvc.mappers.CustomerMapper;
import com.sfg.spring6restmvc.model.CustomerDTO;
import com.sfg.spring6restmvc.repositories.CustomerRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    CustomerMapper customerMapper;

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

    @Transactional
    @Rollback
    @Test
    void testSaveCustomer() {
        CustomerDTO customerDTO = CustomerDTO.builder()
                .customerName("New Customer")
                .build();

        ResponseEntity<Object> responseEntity = customerController.saveNewCustomer(customerDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] location = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID uuid = UUID.fromString(location[4]);
        Customer referenceById = customerRepository.getReferenceById(uuid);
        assertThat(referenceById).isNotNull();
    }


    @Transactional
    @Rollback
    @Test
    void testUpdateCustomer() {
        Customer customer = customerRepository.findAll().get(0);
        CustomerDTO customerDTO = customerMapper.customerToCustomerDto(customer);
        customerDTO.setId(null);
        customerDTO.setVersion(null);
        final String customerName = "UPDATED";
        customerDTO.setCustomerName(customerName);

        ResponseEntity<Object> responseEntity = customerController.updateById(customer.getId(), customerDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        Customer customer1 = customerRepository.findById(customer.getId()).get();
        assertThat(customer1.getCustomerName()).isEqualTo(customerName);
    }


    @Test
    void testUpdateCustomerNotFound() {
        assertThrows(NotFoundException.class,() ->{
            customerController.updateById(UUID.randomUUID(),CustomerDTO.builder().build());
        });
    }

    @Transactional
    @Rollback
    @Test
    void testDeleteById() {
        Customer customer = customerRepository.findAll().get(0);

        ResponseEntity<Object> responseEntity = customerController.deleteById(customer.getId());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        assertThat(customerRepository.findById(customer.getId())).isEmpty();
    }

    @Test
    void testDeleteCustomerNotFound() {
        assertThrows(NotFoundException.class, () ->{
            customerController.deleteById(UUID.randomUUID());
        });
    }
}