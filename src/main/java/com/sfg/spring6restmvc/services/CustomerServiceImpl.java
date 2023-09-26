package com.sfg.spring6restmvc.services;

import com.sfg.spring6restmvc.model.CustomerDTO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final Map<UUID, CustomerDTO> customerMap;

    public CustomerServiceImpl(){
        customerMap = new HashMap<>();

        CustomerDTO john = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .customerName("John")
                .version(1)
                .createdDate(LocalDate.now())
                .lastModifiedDate(LocalDate.now())
                .build();

        CustomerDTO sai = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .customerName("Sai")
                .version(1)
                .createdDate(LocalDate.now())
                .lastModifiedDate(LocalDate.now())
                .build();

        CustomerDTO mam = CustomerDTO.builder()
                .id(UUID.randomUUID())
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
    public List<CustomerDTO> listCustomers() {

        return new ArrayList<>(customerMap.values());
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {
        return Optional.ofNullable(customerMap.get(id));
    }

    @Override
    public CustomerDTO saveNewCustomer(CustomerDTO customer) {

        CustomerDTO savedCustomer = CustomerDTO.builder()
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
    public void updateCustomerById(UUID id, CustomerDTO customer) {
        CustomerDTO existingC = customerMap.get(id);
        existingC.setId(id);
        existingC.setCustomerName(customer.getCustomerName());
        existingC.setVersion(customer.getVersion());

        customerMap.put(existingC.getId(),existingC);
    }

    @Override
    public CustomerDTO deleteById(UUID id) {

        try{
            CustomerDTO cus = customerMap.get(id);
            customerMap.remove(id);
            return cus;
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public void updateById(UUID id, CustomerDTO customer) {
        CustomerDTO existingC = customerMap.get(id);

        if(StringUtils.hasText(customer.getCustomerName())){
            existingC.setCustomerName(customer.getCustomerName());
        }
    }

}
