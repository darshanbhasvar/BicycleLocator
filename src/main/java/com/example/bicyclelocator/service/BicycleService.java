package com.example.bicyclelocator.service;

import com.example.bicyclelocator.dtos.ResponseDto;
import com.example.bicyclelocator.models.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.args.GeoUnit;
import redis.clients.jedis.params.GeoRadiusParam;
import redis.clients.jedis.resps.GeoRadiusResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BicycleService {

    @Autowired
    private Jedis jedis;

    public ResponseDto getNearestBicycle(Double latitude , Double longitude) {

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
        ResponseDto dto = new ResponseDto();
        Map<String, Object> result = new HashMap<>();
        if (!responses.isEmpty()) {
            GeoRadiusResponse closestBicycle = responses.get(0);
            String bicycleId = new String(closestBicycle.getMember());
            result.put("bicycleId", bicycleId);
            List<GeoCoordinate> cordinates = jedis.geopos(bicycleId, bicycleId);
            System.out.println(cordinates);
            GeoCoordinate coordinate = cordinates.get(0);
            dto.setBicycleId(bicycleId);
            dto.setLatitude(coordinate.getLatitude());
            dto.setLongitude(coordinate.getLongitude());
           // result.put("latitude", coordinate.getLatitude());
           // result.put("longitude", coordinate.getLongitude());
        } else {
            result.put("message", "No bicycles found within the specified radius.");
        }
        return dto;
        //return result;
    }
}
