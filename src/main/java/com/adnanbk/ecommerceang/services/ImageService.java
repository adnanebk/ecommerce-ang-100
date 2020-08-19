package com.adnanbk.ecommerceang.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {

   String CreateImage(MultipartFile multipartFile) throws IOException;

}
