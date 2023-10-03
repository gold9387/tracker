package com.example.tracker.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tracker.dto.DeliveryDto;
import com.example.tracker.service.DeliveryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;
    
    @PostMapping("/address")
    public ResponseEntity<String> receiveAddress(@RequestBody DeliveryDto deliveryDto) {
        deliveryService.saveDelivery(deliveryDto);
        Double[] startCoordinate = deliveryService.converter(deliveryDto.getStartAddress());
        Double[] endCoordinate = deliveryService.converter(deliveryDto.getEndAddress());
        System.out.println(startCoordinate[0]);
        System.out.println(startCoordinate[1]);
        System.out.println(endCoordinate[0]);
        System.out.println(endCoordinate[1]);
        return new ResponseEntity<>("Coordinates received successfully", HttpStatus.OK);
    }
}
