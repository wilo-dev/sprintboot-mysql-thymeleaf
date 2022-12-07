package com.wilo.springbootjpamysql.app.helpers;

import com.cloudinary.Cloudinary;
import com.cloudinary.Singleton;
import com.cloudinary.Transformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class CloudinaryConfig {

    private Cloudinary cloudinary;

    @Autowired
    public CloudinaryConfig(@Value("${apiKey}") String key,
                            @Value("${apiSecret}") String secret,
                            @Value("${cloudName}") String cloud) {
        cloudinary = Singleton.getCloudinary();
        cloudinary.config.cloudName = cloud;
        cloudinary.config.apiSecret = secret;
        cloudinary.config.apiKey = key;
    }

    // save claodinary
    public Map uploadImg(Object file, Map options) {

        try {
            return cloudinary.uploader().upload(file, options);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // transforma la imagen
    public String createUrl(String name, int width, int height, String action) {
        return cloudinary.url()
                .transformation(new Transformation()
                        .width(width).height(height)
                        .border("2px _solid _black").crop(action))
                .imageTag(name);

    }

}
