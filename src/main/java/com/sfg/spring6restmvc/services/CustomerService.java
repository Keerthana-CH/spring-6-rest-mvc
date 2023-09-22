package com.sfg.spring6restmvc.services;

import com.sfg.spring6restmvc.model.Customer;

import java.util.List;

public interface CustomerService {

    public List<Customer> listCustomers();
    public Customer getCustomerById(Long id);

    Customer saveNewCustomer(Customer customer);

    void updateCustomerById(Long id, Customer customer);
}
