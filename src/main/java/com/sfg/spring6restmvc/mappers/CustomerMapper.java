package com.sfg.spring6restmvc.mappers;

import com.sfg.spring6restmvc.entities.Customer;
import com.sfg.spring6restmvc.model.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

    Customer customerDtoToCustomer(CustomerDTO customerDTO);
    CustomerDTO customerToCustomerDto(Customer customer);
}
