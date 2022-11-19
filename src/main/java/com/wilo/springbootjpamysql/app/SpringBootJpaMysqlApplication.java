package com.wilo.springbootjpamysql.app;

import com.cloudinary.Cloudinary;
import com.wilo.springbootjpamysql.app.models.services.uploadImg.IUploadsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class SpringBootJpaMysqlApplication implements CommandLineRunner {

    @Autowired
    IUploadsService uploadsService;

    @Value("cloud.name")
    private String cloudName;

    @Value("api.key")
    private String apiKey;

    @Value("api.sercret")
    private String apiSecret;

    @Value("api.environment.variable")
    private String apiEnv;

    @Bean
    public Cloudinary cloudinaryConfig() {
        Cloudinary cloudinary = null;
        Map<String, Object> config = new HashMap<>();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);
        config.put("api_env", apiEnv);
        cloudinary = new Cloudinary(config);
        return cloudinary;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBootJpaMysqlApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        uploadsService.deleteDirectory();
        uploadsService.createDirectory();
    }


}
