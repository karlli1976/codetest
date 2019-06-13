package com.optus.candidate.codetest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class CodetestApplication {

  public static void main(String[] args) {

    SpringApplication.run(CodetestApplication.class, args);
  }

}
