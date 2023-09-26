package com.sfg.spring6restmvc.services;

import com.sfg.spring6restmvc.model.CustomerDTO;

import java.util.List;

public interface CustomerService {

    List<CustomerDTO> listCustomers();
    CustomerDTO getCustomerById(Long id);

    CustomerDTO saveNewCustomer(CustomerDTO customer);

    void updateCustomerById(Long id, CustomerDTO customer);

    CustomerDTO deleteById(Long id);

    void updateById(Long id, CustomerDTO customer);

}
