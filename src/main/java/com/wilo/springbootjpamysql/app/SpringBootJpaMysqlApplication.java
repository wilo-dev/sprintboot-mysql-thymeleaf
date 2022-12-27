package com.wilo.springbootjpamysql.app;

import com.wilo.springbootjpamysql.app.models.services.uploadImg.IUploadsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@SpringBootApplication
public class SpringBootJpaMysqlApplication implements CommandLineRunner {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    IUploadsService uploadsService;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootJpaMysqlApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        uploadsService.deleteDirectory();
        uploadsService.createDirectory();

        // esto es solo de prueba
        String password = "123456";
        for (int i = 0; i < 2; i++) {
            String bcryptPassword = passwordEncoder.encode(password);
            System.out.println(bcryptPassword);
        }
    }


}
