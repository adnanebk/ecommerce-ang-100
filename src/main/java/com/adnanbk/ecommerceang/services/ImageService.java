package com.adnanbk.ecommerceang.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.FileEditor;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;

@Service
public class ImageService {


    private ResourceLoader resourceLoader;

   @Autowired
    public ImageService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }



    public String CreateImage(MultipartFile image) throws IOException {
        String resoureString;

        String fileName = image.getOriginalFilename().toLowerCase();

            if (fileName != null && !fileName.isEmpty()) {
                if(!fileName.endsWith(".jpg") && !fileName.endsWith(".png"))
                    throw new IOException("Image type not supported , we accept only jpg or png files");
                String dir = resourceLoader.getResource("classpath:/static").getFile().getAbsolutePath();
                resoureString = dir + "/uploadingDir/" + fileName;
                image.transferTo(Paths.get(resoureString));
            }
return fileName;

    }
}
