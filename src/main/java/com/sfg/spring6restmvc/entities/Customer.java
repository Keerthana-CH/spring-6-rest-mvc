package com.sfg.spring6restmvc.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID" ,  strategy = "org.hibernate.id.UUIDGenerator")
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36,columnDefinition = "varchar(36)",updatable = false,nullable = false)
    private UUID id;
    private String customerName;

    @Column(length = 255)
    private String email;

    @Version
    private Integer version;
    private LocalDate createdDate;
    private LocalDate lastModifiedDate;

    @OneToMany(mappedBy = "customer")
    private Set<BeerOrder> beerOrders;
}
