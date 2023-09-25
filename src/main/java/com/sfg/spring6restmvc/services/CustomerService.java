package com.sfg.spring6restmvc.services;

import com.sfg.spring6restmvc.model.Customer;

import java.util.List;

public interface CustomerService {

    List<Customer> listCustomers();
    Customer getCustomerById(Long id);

    Customer saveNewCustomer(Customer customer);

    void updateCustomerById(Long id, Customer customer);

    Customer deleteById(Long id);

    void updateById(Long id, Customer customer);

}
