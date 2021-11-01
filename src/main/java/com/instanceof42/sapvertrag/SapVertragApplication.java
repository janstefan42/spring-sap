package com.instanceof42.sapvertrag;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ConfigurationPropertiesScan("com.instanceof42.sapvertrag")
@EnableScheduling
public class SapVertragApplication {

  public static void main(String[] args) {
    SpringApplication.run(SapVertragApplication.class, args);
  }
}
