package com.example.bicyclelocator.controllers;


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

    @Autowired
    public BicycleLocatorController(Jedis jedis) {
        this.jedis = jedis;
    }

    @GetMapping("/locate")
    public Map<String, Object> locate(@RequestParam double latitude, @RequestParam double longitude) {
        // Check if the geospatial index "bicycles" exists in Redis
        if (!jedis.exists("bicycles")) {
            throw new RuntimeException("Geospatial index 'bicycles' does not exist in Redis");
        }
        // Check if there are any members in the geospatial index "bicycles"
        if (jedis.zcard("bicycles") == 0) {
            throw new RuntimeException("No members in geospatial index 'bicycles'");
        }
        List<GeoRadiusResponse> responses;
        double radius = 6371;  // Radius of the Earth in km
        responses = jedis.georadius("bicycles", longitude, latitude, radius, GeoUnit.KM, GeoRadiusParam.geoRadiusParam().sortAscending().count(1));

        Map<String, Object> result = new HashMap<>();
        if (!responses.isEmpty()) {
            GeoRadiusResponse closestBicycle = responses.get(0);
            result.put("bicycleId", new String(closestBicycle.getMember()));
            GeoCoordinate coordinate = closestBicycle.getCoordinate();
            result.put("latitude", coordinate.getLatitude());
            result.put("longitude", coordinate.getLongitude());
        } else {
            result.put("message", "No bicycles found within the specified radius.");
        }
        return result;
    }

//    @GetMapping("/locate")  //old redis version
//    public Map<String, Object> locate(@RequestParam double latitude, @RequestParam double longitude) {
//        List<GeoRadiusResponse> responses;
//        GeoSearchParam params = GeoSearchParam.geoSearchParam()
//                .fromLonLat(longitude, latitude)
//                .byRadius(1, GeoUnit.KM)
//                .asc()
//                .count(1);
//        responses = jedis.geosearch("bicycles", params);
//        GeoRadiusResponse closestBicycle = responses.get(0);
//        Map<String, Object> result = new HashMap<>();
//        result.put("bicycleId", new String(closestBicycle.getMember()));
//        GeoCoordinate coordinate = closestBicycle.getCoordinate();
//        result.put("latitude", coordinate.getLatitude());
//        result.put("longitude", coordinate.getLongitude());
//        return result;
//    }
}







//@RestController
//@RequestMapping("/bicycle")
//public class BicycleLocatorController {
//    @Autowired
//    private ParquetToJavaConfig parquetToJavaConfig;
//
//    @Autowired
//    private BicycleService bicycleService;
//
//    public BicycleLocatorController(ParquetToJavaConfig parquetToJavaConfig, BicycleService bicycleService) {
//        this.parquetToJavaConfig = parquetToJavaConfig;
//        this.bicycleService = bicycleService;
//    }
//
//    public Location getNearestBicycle(LoginDto loginDto) {
//
//        return bicycleService.getNearestBicycle(loginDto.getLatitude(),loginDto.getLongitude());
//    }
//    @GetMapping("/read")
//    public void run() {
//        parquetToJavaConfig.readParquetFile();
//    }
//}
