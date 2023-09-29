package com.sfg.spring6restmvc.services;

import com.sfg.spring6restmvc.mappers.CustomerMapper;
import com.sfg.spring6restmvc.model.CustomerDTO;
import com.sfg.spring6restmvc.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class CustomerServiceJPA implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    @Override
    public List<CustomerDTO> listCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::customerToCustomerDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {
        return Optional.ofNullable(customerMapper.customerToCustomerDto(customerRepository.findById(id)
                .orElse(null)));
    }

    @Override
    public CustomerDTO saveNewCustomer(CustomerDTO customer) {
        return customerMapper.customerToCustomerDto(customerRepository.save(customerMapper.customerDtoToCustomer(customer)));
    }

    @Override
    public void patchById(UUID id, CustomerDTO customer) {

    }

    @Override
    public Boolean deleteById(UUID id) {
        if (customerRepository.existsById(id)){
            customerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Optional<CustomerDTO> updateById(UUID id, CustomerDTO customer) {
        AtomicReference<CustomerDTO> atomicReference = new AtomicReference<>();

        customerRepository.findById(id).ifPresent(foundCustomer ->{
            foundCustomer.setCustomerName(customer.getCustomerName());
            atomicReference.set(customerMapper.customerToCustomerDto(customerRepository.save(foundCustomer)));
        });
        return Optional.ofNullable(atomicReference.get());
    }
}
