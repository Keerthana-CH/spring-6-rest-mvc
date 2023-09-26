package com.sfg.spring6restmvc.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    @Id
    private UUID id;
    private String customerName;

    @Version
    private Integer version;
    private LocalDate createdDate;
    private LocalDate lastModifiedDate;
}
