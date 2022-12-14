package com.wilo.springbootjpamysql.app;

import com.wilo.springbootjpamysql.app.models.services.uploadImg.IUploadsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class SpringBootJpaMysqlApplication implements CommandLineRunner {

    @Autowired
    IUploadsService uploadsService;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootJpaMysqlApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        uploadsService.deleteDirectory();
        uploadsService.createDirectory();
    }


}
