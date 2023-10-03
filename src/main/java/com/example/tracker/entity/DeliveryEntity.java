package com.example.tracker.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "delivery") 
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id; 
    private String startAddress;
    private String endAddress;
    private String name;
    private String productName;
    private String item;
}
