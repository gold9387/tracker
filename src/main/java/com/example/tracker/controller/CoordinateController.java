package com.example.tracker.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tracker.dto.CoordinateData;

@RestController
@RequestMapping("/api")
public class CoordinateController {
    
    @PostMapping("/address")
    public ResponseEntity<String> receiveCoordinates(@RequestBody CoordinateData coordinateData) {
        System.out.println("Start Coordinate: " + coordinateData.getStartAddress());
        System.out.println("End Coordinate: " + coordinateData.getEndAddress());
        System.out.println("Name: " + coordinateData.getName());
        System.out.println("Phone: " + coordinateData.getPhone());
        System.out.println("Item: " + coordinateData.getItem());
        return new ResponseEntity<>("Coordinates received successfully", HttpStatus.OK);
    }
}
