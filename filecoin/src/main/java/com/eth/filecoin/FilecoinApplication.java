package com.eth.filecoin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FilecoinApplication {

  public static void main(String[] args) {
    SpringApplication.run(FilecoinApplication.class, args);
    System.out.println("http://localhost:8088/doc.html");
  }

}
