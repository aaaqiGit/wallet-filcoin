package com.xxl.job.executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author xuxueli 2018-10-28 00:38:13
 */
@SpringBootApplication(scanBasePackages="com.xxl.job")
public class XxlJobExecutorApplication {

  public static void main(String[] args) {
    SpringApplication.run(XxlJobExecutorApplication.class, args);
    System.out.println("http://127.0.0.1:8086/xxl-job-admin");
  }

}