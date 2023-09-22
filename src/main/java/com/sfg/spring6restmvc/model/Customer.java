package com.sfg.spring6restmvc.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class Customer {
    private Long id;
    private String customerName;
    private Integer version;
    private LocalDate createdDate;
    private LocalDate lastModifiedDate;
}
