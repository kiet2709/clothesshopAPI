package com.kiet.clothesshop.service;

import java.io.IOException;
import java.util.List;

import com.kiet.clothesshop.model.Image;
import com.kiet.clothesshop.payload.request.ClothRequest;
import com.kiet.clothesshop.payload.response.BrandResponse;
import com.kiet.clothesshop.payload.response.ClothResponse;
import org.springframework.web.multipart.MultipartFile;


public interface IClothService {

	List<ClothResponse> getAllCloths();

	ClothResponse getClothById(Long clothId);

	BrandResponse getBrandByClothId(Long clothId);

	ClothResponse addCloth(ClothRequest clothRequest);

	ClothResponse uploadImage(MultipartFile file, Long categoryId) throws IOException;

	Image getImagesById(Long clothId);

}
