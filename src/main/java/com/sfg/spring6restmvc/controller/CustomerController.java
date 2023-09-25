package com.sfg.spring6restmvc.controller;


import com.sfg.spring6restmvc.model.Customer;
import com.sfg.spring6restmvc.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/customer")
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

    @PostMapping
    public ResponseEntity<Object> saveNewCustomer(@RequestBody Customer customer){
        Customer savedCustomer = customerService.saveNewCustomer(customer);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location","/api/v1/customer/" + savedCustomer.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<Object> updateCustomerById(@PathVariable("customerId") Long id,@RequestBody Customer customer){
        customerService.updateCustomerById(id,customer);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{customerId}")
    public ResponseEntity<Object> deleteById(@PathVariable("customerId") Long id){
        Customer cus = customerService.deleteById(id);
        if(cus==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("{customerId}")
    public ResponseEntity<Object> updateBeerById(@PathVariable("customerId") Long id, @RequestBody Customer customer){
        customerService.updateById(id,customer);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
