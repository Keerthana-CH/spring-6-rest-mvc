package com.sfg.spring6restmvc.controller;


import com.sfg.spring6restmvc.model.Customer;
import com.sfg.spring6restmvc.services.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {
    private final CustomerService customerService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Customer> listCustomers(){
        return customerService.listCustomers();
    }

    @RequestMapping(value = "/{customerId}",method = RequestMethod.GET)
    public Customer getCustomerById(@PathVariable("customerId") Long d){
        return customerService.getCustomerById(d);
    }
}
