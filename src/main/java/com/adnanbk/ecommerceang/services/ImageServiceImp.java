package com.adnanbk.ecommerceang.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;

@Service
public class ImageServiceImp implements ImageService {


    private ResourceLoader resourceLoader;

   @Autowired
    public ImageServiceImp(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }



    public String CreateImage(MultipartFile image) throws IOException {
        String resoureString;

        String fileName = image.getOriginalFilename().toLowerCase();

            if (!fileName.isEmpty()) {
                if(!fileName.endsWith(".jpg") && !fileName.endsWith(".png"))
                    throw new IOException("Image type not supported , we accept only jpg or png files");
                String dir = resourceLoader.getResource("classpath:/static").getFile().getAbsolutePath();
                resoureString = dir + "/uploadingDir/" + fileName;
                image.transferTo(Paths.get(resoureString));
            }
            fileName
return fileName;

    }

    @Override
    public String getImageUl() {
        return null;
    }
}
