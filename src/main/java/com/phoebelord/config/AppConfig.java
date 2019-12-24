package com.phoebelord.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
@ComponentScan(basePackages = "com.phoebelord")
public class AppConfig {

  @Bean
  public Random getRandom() {
    return new Random();
  }
}
