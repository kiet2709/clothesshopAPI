package com.kiet.clothesshop.service;

import com.kiet.clothesshop.model.Banner;
import com.kiet.clothesshop.model.Image;
import org.springframework.web.multipart.MultipartFile;



import io.jsonwebtoken.io.IOException;

public interface IBannerService {

	Image getImagesById(Long categoryId);

	Banner uploadImage(MultipartFile file, Long bannerId) throws IOException, java.io.IOException;

	Banner addBanner(MultipartFile file) throws java.io.IOException;


}
