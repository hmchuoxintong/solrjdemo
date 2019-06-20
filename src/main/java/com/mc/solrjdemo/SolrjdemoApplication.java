package com.mc.solrjdemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.mc.solrjdemo.solrjdemo.mapper")
public class SolrjdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SolrjdemoApplication.class, args);
    }

}
