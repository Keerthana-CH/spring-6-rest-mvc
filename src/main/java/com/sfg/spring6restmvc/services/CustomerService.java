package com.sfg.spring6restmvc.services;

import com.sfg.spring6restmvc.model.CustomerDTO;

import java.util.List;
import java.util.UUID;

public interface CustomerService {

    List<CustomerDTO> listCustomers();
    CustomerDTO getCustomerById(UUID id);

    CustomerDTO saveNewCustomer(CustomerDTO customer);

    void updateCustomerById(UUID id, CustomerDTO customer);

    CustomerDTO deleteById(UUID id);

    void updateById(UUID id, CustomerDTO customer);

}
