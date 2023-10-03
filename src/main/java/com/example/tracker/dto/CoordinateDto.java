package com.example.tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoordinateDto {
    private double startLatitude;
    private double startLongitude;
    private double endLatitude;
    private double endLongitude;
}
