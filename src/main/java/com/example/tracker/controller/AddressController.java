package com.example.tracker.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tracker.dto.AddressDto;
import com.example.tracker.service.AddressService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;
    
    @PostMapping("/address")
    public ResponseEntity<String> receiveAddress(@RequestBody AddressDto addressDto) {
        Double[] startCoordinate = addressService.converter(addressDto.getStartAddress());
        Double[] endCoordinate = addressService.converter(addressDto.getEndAddress());
        System.out.println(startCoordinate[0]);
        System.out.println(startCoordinate[1]);
        System.out.println(endCoordinate[0]);
        System.out.println(endCoordinate[1]);
        return new ResponseEntity<>("Coordinates received successfully", HttpStatus.OK);
    }
}
