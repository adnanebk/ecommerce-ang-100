package com.adnanbk.ecommerceang.services.imp;

import com.adnanbk.ecommerceang.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.CompletableFuture;

@Service
public class ImageServiceImp implements ImageService {


    @Value("${app.upload.dir:${user.home}}")
    public String uploadDir;

    private  Path root;

    @PostConstruct
    public void init() {
        try {
            root=Paths.get(uploadDir);
            if(!Files.isDirectory(Paths.get(uploadDir)))
            Files.createDirectory(Paths.get(uploadDir));
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Async
    public CompletableFuture<String> CreateImage(MultipartFile image) throws IOException {

        if(image==null || image.getOriginalFilename()==null)
            throw new IOException("you must upload  a valid image ");
        String fileName=image.getOriginalFilename().trim();
        if(!fileName.endsWith(".jpg") && !fileName.endsWith(".png"))
            throw new IOException("Image type not supported , we accept only jpg or png files");
        Path filePath = this.root.resolve(fileName);
             image.transferTo(filePath);
             return  CompletableFuture.completedFuture(fileName);
    }

    @Override
    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

}
