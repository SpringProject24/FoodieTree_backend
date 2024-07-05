package org.nmfw.foodietree;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FoodieTreeApplication {
    public static void main(String[] args) {
        SpringApplication.run(FoodieTreeApplication.class, args);
    }

}
