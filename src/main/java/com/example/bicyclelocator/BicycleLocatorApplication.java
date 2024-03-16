package com.example.bicyclelocator;

import com.example.bicyclelocator.service.RedisLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

public class BicycleLocatorApplication {

	//private final RedisLoader parquetToJavaConfig;

//	@Autowired
//	public BicycleLocatorApplication(RedisLoader parquetToJavaConfig) {
//		this.parquetToJavaConfig = parquetToJavaConfig;
//	}
	public static void main(String[] args) {

		SpringApplication.run(BicycleLocatorApplication.class, args);
	}
}
