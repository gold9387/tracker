package com.example.tracker.service;

import com.example.tracker.dto.DeliveryDto;

public interface DeliveryService {
    Double[] converter(String address);
    void saveDelivery(DeliveryDto deliveryDto);
}
