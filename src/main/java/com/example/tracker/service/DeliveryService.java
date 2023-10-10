package com.example.tracker.service;

import java.util.List;

import com.example.tracker.dto.DeliveryDto;
import com.example.tracker.dto.Info;

public interface DeliveryService {
    Double[] converter(String address);
    void saveDelivery(DeliveryDto deliveryDto);
    void saveCoordinates(List<Info.Coordinate> coordinates);
}
