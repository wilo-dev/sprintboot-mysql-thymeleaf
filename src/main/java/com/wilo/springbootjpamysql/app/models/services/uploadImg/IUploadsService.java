package com.wilo.springbootjpamysql.app.models.services.uploadImg;

import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

public interface IUploadsService {

    // mostrar la img en la vista
    public UrlResource getImg(String filename) throws MalformedURLException;

    //remonbra la img
    public String copy(MultipartFile file) throws IOException;

    public boolean deleteImg(String filename);

    // delete directory uploads
    public void deleteDirectory();

    // created directory uploads
    public void createDirectory() throws IOException;
}
