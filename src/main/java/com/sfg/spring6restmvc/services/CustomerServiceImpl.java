package com.sfg.spring6restmvc.services;

import com.sfg.spring6restmvc.model.Beer;
import com.sfg.spring6restmvc.model.Customer;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService {

    private Map<Long,Customer> customerMap;

    public CustomerServiceImpl(){
        customerMap = new HashMap<>();

        Customer john = Customer.builder()
                .id(100L)
                .customerName("John")
                .version(1)
                .createdDate(LocalDate.now())
                .lastModifiedDate(LocalDate.now())
                .build();

        Customer sai = Customer.builder()
                .id(101L)
                .customerName("Sai")
                .version(1)
                .createdDate(LocalDate.now())
                .lastModifiedDate(LocalDate.now())
                .build();

        Customer mam = Customer.builder()
                .id(102L)
                .customerName("Mam")
                .version(1)
                .createdDate(LocalDate.now())
                .lastModifiedDate(LocalDate.now())
                .build();

        customerMap.put(john.getId(), john);
        customerMap.put(sai.getId(), sai);
        customerMap.put(mam.getId(), mam);
    }
    @Override
    public List<Customer> listCustomers() {

        return new ArrayList<>(customerMap.values());
    }

    @Override
    public Customer getCustomerById(Long id) {
        return customerMap.get(id);
    }

    @Override
    public Customer saveNewCustomer(Customer customer) {

        Customer savedCustomer = Customer.builder()
                .id(customer.getId())
                .customerName(customer.getCustomerName())
                .version(1)
                .createdDate(LocalDate.now())
                .lastModifiedDate(LocalDate.now())
                .build();

        customerMap.put(savedCustomer.getId(),savedCustomer);

        return savedCustomer;
    }

    @Override
    public void updateCustomerById(Long id, Customer customer) {
        Customer existingC = customerMap.get(id);
        existingC.setId(id);
        existingC.setCustomerName(customer.getCustomerName());
        existingC.setVersion(customer.getVersion());

        customerMap.put(existingC.getId(),existingC);
    }

    @Override
    public Customer deleteById(Long id) {

        try{
            Customer cus = customerMap.get(id);
            customerMap.remove(id);
            return cus;
        }catch (Exception e){
            return null;
        }
    }
}
