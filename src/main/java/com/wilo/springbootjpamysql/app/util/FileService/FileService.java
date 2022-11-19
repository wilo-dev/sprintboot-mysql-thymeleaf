package com.wilo.springbootjpamysql.app.util.FileService;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service("fileService")
public class FileService {

    public static final String saveLocation = "src\\main\\resources\\static\\uploads";

    public boolean saveFile(MultipartFile multipartFile) {
        boolean result = false;
        String fileName = multipartFile.getOriginalFilename();
        String location = saveLocation;
        File pathFile = new File(location);
        if (pathFile.exists()) {
            pathFile.mkdir();
        }

        pathFile = new File(location + fileName);
        try {
            multipartFile.transferTo(pathFile);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
