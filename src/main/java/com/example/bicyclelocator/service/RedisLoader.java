package com.example.bicyclelocator.service;

import com.exasol.parquetio.data.Row;
import com.exasol.parquetio.reader.RowParquetReader;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.hadoop.util.HadoopInputFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.GeoAddParams;

import java.io.IOException;
import java.util.List;


//@Configuration
@Getter
@Setter
@Service
public class RedisLoader {

    private final Jedis jedis;

    @Autowired
    public RedisLoader(Jedis jedis) {
        this.jedis = jedis;
    }
//    public void loadDataAndCheck() {
//        loadData();
//        boolean isInserted = isBicycleIdInRedis("someBicycleId");
//        System.out.println("Data inserted: " + isInserted);
//    }
//    public boolean isBicycleIdInRedis(String bicycleId) {
//        List<GeoCoordinate> coordinates = jedis.geopos("bicycles", bicycleId);
//        return coordinates != null && !coordinates.isEmpty();
//    }

    @PostConstruct
    public void loadData() {
        Path path = new Path("D:\\BicycleLocatorChallenge\\bicycle_data.parquet");

        Configuration conf = new Configuration();

        try (final ParquetReader<Row> reader = RowParquetReader.builder(HadoopInputFile.fromPath(path, conf)).build()) {
            Row row = reader.read();
            while (row != null) {
                String bicycleId = (String) row.getValue("BicycleID");
                double latitude =(double) row.getValue("Latitude");
                double longitude = (double)row.getValue("Longitude");

               // jedis.geoadd("bicycles", longitude, latitude, bicycleId, GeoAddParams.geoAddParams().withHash());
                try{
                    jedis.geoadd(bicycleId, longitude, latitude, bicycleId);
                } catch (Exception e){
                        System.out.println("error in adding locations");
                }

                System.out.println("Bicycle with id " + bicycleId + " added to Redis"+"response from redis" );
                row = reader.read();
            }
        } catch (Exception exception) {
            // Handle exception
            exception.printStackTrace();
        }
    }
}