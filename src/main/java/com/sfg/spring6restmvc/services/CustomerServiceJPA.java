package com.sfg.spring6restmvc.services;

import com.sfg.spring6restmvc.mappers.CustomerMapper;
import com.sfg.spring6restmvc.model.CustomerDTO;
import com.sfg.spring6restmvc.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Primary
@RequiredArgsConstructor
public class CustomerServiceJPA implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    @Override
    public List<CustomerDTO> listCustomers() {
        return null;
    }

    @Override
    public CustomerDTO getCustomerById(UUID id) {
        return null;
    }

    @Override
    public CustomerDTO saveNewCustomer(CustomerDTO customer) {
        return null;
    }

    @Override
    public void updateCustomerById(UUID id, CustomerDTO customer) {

    }

    @Override
    public CustomerDTO deleteById(UUID id) {
        return null;
    }

    @Override
    public void updateById(UUID id, CustomerDTO customer) {

    }
}
