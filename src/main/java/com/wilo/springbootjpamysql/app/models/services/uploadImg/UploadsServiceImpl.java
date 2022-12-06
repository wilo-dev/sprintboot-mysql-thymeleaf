package com.wilo.springbootjpamysql.app.models.services.uploadImg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class UploadsServiceImpl implements IUploadsService {

    // para q muestre x consola
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final static String UPLOADS_FOLDER = "uploads";

    /*
 ================================================================================
                         OBTENER IMG
 ================================================================================
 ***/
    @Override
    public UrlResource getImg(String filename) throws MalformedURLException {
        Path pathPhoto = getPath(filename);

        log.info("pathPhoto: " + pathPhoto);
        UrlResource resource = null;

        resource = new UrlResource(pathPhoto.toUri());
        if (!resource.exists() && !resource.isReadable()) {
            throw new RuntimeException("Error: no se puede cargar la img: " + pathPhoto.toString());
        }
        return resource;
    }

    /*
 ================================================================================
                  SAVE AND  RENOMBRANDO IMG
 ================================================================================
 ***/
    @Override
    public String copy(MultipartFile file) throws IOException {
        String uniqueFilename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path rootAbsolutPath = getPath(uniqueFilename); // path absoluto
//        log.info("rootAbsolutPath: " + rootAbsolutPath);
        Files.copy(file.getInputStream(), rootAbsolutPath);
        return uniqueFilename;
    }

    /*
 ================================================================================
   cada ves q se edita o se elimina un client se elimina la img del servidor
 ================================================================================
 ***/
    @Override
    public boolean deleteImg(String filename) {
        Path rootPath = getPath(filename);
        File file = rootPath.toFile();
        // eliminar img validar si exist
        if (file.exists() && file.canRead()) {
            if (file.delete()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void deleteDirectory() {
        FileSystemUtils.deleteRecursively(Paths.get(UPLOADS_FOLDER).toFile());
    }


    /*
================================================================================
created path in directory server
================================================================================
***/
    @Override
    public void createDirectory() throws IOException {
        Files.createDirectory(Paths.get(UPLOADS_FOLDER));
    }


    /*
================================================================================
get path  directory
================================================================================
***/
    public Path getPath(String filename) {
        return Paths.get(UPLOADS_FOLDER).resolve(filename).toAbsolutePath();
    }
}
