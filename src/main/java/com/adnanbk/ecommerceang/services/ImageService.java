package com.adnanbk.ecommerceang.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public interface ImageService {

   CompletableFuture<String> CreateImage(MultipartFile multipartFile) throws IOException;

   Resource load(String filename);
}
