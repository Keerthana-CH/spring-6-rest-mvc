package com.sfg.spring6restmvc.controller;


import com.sfg.spring6restmvc.model.CustomerDTO;
import com.sfg.spring6restmvc.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {
    private final CustomerService customerService;

    @RequestMapping(method = RequestMethod.GET)
    public List<CustomerDTO> listCustomers(){
        return customerService.listCustomers();
    }

    @RequestMapping(value = "/{customerId}",method = RequestMethod.GET)
    public CustomerDTO getCustomerById(@PathVariable("customerId") UUID d){
        return customerService.getCustomerById(d).orElseThrow(NotFoundException::new);
    }

    @PostMapping
    public ResponseEntity<Object> saveNewCustomer(@RequestBody CustomerDTO customer){
        CustomerDTO savedCustomer = customerService.saveNewCustomer(customer);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location","/api/v1/customer/" + savedCustomer.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<Object> updateCustomerById(@PathVariable("customerId") UUID id,@RequestBody CustomerDTO customer){
        customerService.updateCustomerById(id,customer);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{customerId}")
    public ResponseEntity<Object> deleteById(@PathVariable("customerId") UUID id){
        CustomerDTO cus = customerService.deleteById(id);
        if(cus==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("{customerId}")
    public ResponseEntity<Object> updateById(@PathVariable("customerId") UUID id, @RequestBody CustomerDTO customer){
        customerService.updateById(id,customer);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
