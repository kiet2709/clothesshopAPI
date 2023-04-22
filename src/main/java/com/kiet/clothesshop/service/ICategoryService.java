package com.kiet.clothesshop.service;

import java.util.List;

import com.kiet.clothesshop.model.Image;
import com.kiet.clothesshop.payload.request.CategoryRequest;
import com.kiet.clothesshop.payload.response.CategoryResponse;
import com.kiet.clothesshop.payload.response.ClothResponse;
import org.springframework.web.multipart.MultipartFile;


import io.jsonwebtoken.io.IOException;

public interface ICategoryService {

	List<CategoryResponse> getAllCategories();

	CategoryResponse getCategoryById(Long categoryId);

	List<ClothResponse> getClothesByCategoryId(Long categoryId);

	CategoryResponse addCategory(CategoryRequest categoryRequest);

	CategoryResponse uploadImage(MultipartFile file, Long categoryId) throws IOException, java.io.IOException;

	Image getImagesById(Long categoryId);

}
