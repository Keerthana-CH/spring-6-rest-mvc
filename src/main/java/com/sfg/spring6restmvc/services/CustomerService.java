package com.sfg.spring6restmvc.services;

import com.sfg.spring6restmvc.model.Customer;

import java.util.List;
import java.util.UUID;

public interface CustomerService {

    public List<Customer> listCustomers();
    public Customer getCustomerById(Long id);

    Customer saveNewCustomer(Customer customer);

    void updateCustomerById(Long id, Customer customer);

    Customer deleteById(Long id);

    void updateById(Long id, Customer customer);

}
