package com.example.bicyclelocator.controllers;


import com.example.bicyclelocator.dtos.ResponseDto;
import com.example.bicyclelocator.service.BicycleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.args.GeoUnit;
import redis.clients.jedis.params.GeoRadiusParam;
import redis.clients.jedis.params.GeoSearchParam;
import redis.clients.jedis.resps.GeoRadiusResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class BicycleLocatorController {

    private final Jedis jedis;
    private BicycleService bicycleService;

    @Autowired
    public BicycleLocatorController(Jedis jedis, BicycleService bicycleService) {
        this.jedis = jedis;
        this.bicycleService = bicycleService;
    }

//    @Autowired
//    public BicycleLocatorController(Jedis jedis) {
//        this.jedis = jedis;
//    }
    @GetMapping("/nearestBicycle")
    public ResponseDto locate(@RequestParam double latitude, @RequestParam double longitude) {
        return bicycleService.getNearestBicycle(latitude,longitude);
    }
}
//        // Check if the geospatial index "bicycles" exists in Redis
// Check if the geospatial index "bicycles" exists in Redis
//        if (!jedis.exists("bicycles")) {
//                throw new RuntimeException("Geospatial index 'bicycles' does not exist in Redis");
//                }
//                // Check if there are any members in the geospatial index "bicycles"
//                if (jedis.zcard("bicycles") == 0) {
//                throw new RuntimeException("No members in geospatial index 'bicycles'");
//                }
//                List<GeoRadiusResponse> responses;
//        double radius = 6371;  // Radius of the Earth in km
//        responses = jedis.georadius("bicycles", longitude, latitude, radius, GeoUnit.KM, GeoRadiusParam.geoRadiusParam().sortAscending().count(1));
//
//        Map<String, Object> result = new HashMap<>();
//        if (!responses.isEmpty()) {
//        GeoRadiusResponse closestBicycle = responses.get(0);
//        String bicycleId = new String(closestBicycle.getMember());
//        result.put("bicycleId", bicycleId);
//        List<GeoCoordinate> cordinates = jedis.geopos(bicycleId, bicycleId);
//        System.out.println(cordinates);
//        GeoCoordinate coordinate = cordinates.get(0);
//        result.put("latitude", coordinate.getLatitude());
//        result.put("longitude", coordinate.getLongitude());
//        } else {
//        result.put("message", "No bicycles found within the specified radius.");
//        }