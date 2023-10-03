package com.example.tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDto {
    private String startAddress;
    private String endAddress;
    private String name;
    private String productName;
    private String item;
}
