package com.example.tracker.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tracker.dto.DeliveryDto;
import com.example.tracker.dto.Info;
import com.example.tracker.kakao.KakaoDirections;
import com.example.tracker.service.DeliveryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final KakaoDirections kakaoDirections;

    @PostMapping("/address")
    public ResponseEntity<List<Info.Coordinate>> receiveAddress(@RequestBody DeliveryDto deliveryDto) {
        deliveryService.saveDelivery(deliveryDto);
        Double[] startCoordinate = deliveryService.converter(deliveryDto.getStartAddress());
        Double[] endCoordinate = deliveryService.converter(deliveryDto.getEndAddress());
        List<Info.Coordinate> coordinates = kakaoDirections.findRoute(startCoordinate[0], startCoordinate[1],
                endCoordinate[0], endCoordinate[1]);
        if (coordinates != null && !coordinates.isEmpty()) {
            deliveryService.saveCoordinates(coordinates);
        }
        return new ResponseEntity<>(coordinates, HttpStatus.OK);
    }
}
