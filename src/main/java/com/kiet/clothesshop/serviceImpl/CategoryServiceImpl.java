package com.kiet.clothesshop.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import com.kiet.clothesshop.exception.ResourceNotFoundException;
import com.kiet.clothesshop.model.Category;
import com.kiet.clothesshop.model.Cloth;
import com.kiet.clothesshop.model.Image;
import com.kiet.clothesshop.payload.request.CategoryRequest;
import com.kiet.clothesshop.payload.response.CategoryResponse;
import com.kiet.clothesshop.payload.response.ClothResponse;
import com.kiet.clothesshop.repository.CategoryRepository;
import com.kiet.clothesshop.repository.ClothRepository;
import com.kiet.clothesshop.repository.ImageRepository;
import com.kiet.clothesshop.service.ICategoryService;
import com.kiet.clothesshop.untils.AppConstant;
import com.kiet.clothesshop.untils.FileUploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;



import io.jsonwebtoken.io.IOException;

@Service
public class CategoryServiceImpl implements ICategoryService {

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	ClothRepository clothRepository;

	@Autowired
	ImageRepository imageRepository;

	@Override
	public List<CategoryResponse> getAllCategories() {
		List<Category> categories = categoryRepository.findAll();
		List<CategoryResponse> categoryResponses = new ArrayList<>();
		for (Category category: categories) {
			CategoryResponse categoryResponse = new CategoryResponse(
					category.getId(),
					category.getName(),
					category.getImage().getPath()
			);
			categoryResponses.add(categoryResponse);
		}
		return categoryResponses;
	}

	@Override
	public CategoryResponse getCategoryById(Long categoryId) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.CATEGORY_NOT_FOUND+categoryId));
		return new CategoryResponse(
				category.getId(),
				category.getName(),
				category.getImage().getPath()
		);
	}

	@Override
	public List<ClothResponse> getClothesByCategoryId(Long categoryId) {

		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.CATEGORY_NOT_FOUND+categoryId));

		List<Cloth> clothes = clothRepository.findByCategory(category);
		List<ClothResponse> clothResponses = new ArrayList<>();
		for (Cloth cloth: clothes) {
			ClothResponse clothResponse = new ClothResponse(
					cloth.getId(),
					cloth.getName(),
					cloth.getPrice().intValue(),
					cloth.getDescription(),
					cloth.getInventories(),
					cloth.getBrand(),
					cloth.getCategory(),
					cloth.getImage()
			);
			clothResponses.add(clothResponse);
		}
		return clothResponses;
	}

	@Override
	public CategoryResponse addCategory(CategoryRequest categoryRequest) {

		Category category = new Category();
		category.setName(categoryRequest.getName());
		categoryRepository.save(category);

		return new CategoryResponse(
				category.getId(),
				category.getName(),
				category.getImage().getPath()
		);
	}

	@Override
	public CategoryResponse uploadImage(MultipartFile file, Long categoryId) throws IOException, java.io.IOException {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.CATEGORY_NOT_FOUND+categoryId));
		Image image;
		if(category.getImage()!=null) {
			image = category.getImage();
			uploadImage(file, categoryId, category, image);
		}else {
			image = new Image();
			uploadImage(file, categoryId, category, image);
		}


		CategoryResponse categoryResponse = new CategoryResponse();
		categoryResponse.setId(category.getId());
		categoryResponse.setName(category.getName());
		categoryResponse.setImagePath(image.getPath());
		return categoryResponse;
	}

	private void uploadImage(MultipartFile file, Long categoryId, Category category, Image image) throws java.io.IOException {
		if (!file.isEmpty()) {
			FileUploadUtils.saveCategoryImage(file, categoryId);
			image.setTitle(categoryId.toString() + ".png");
			image.setPath(AppConstant.UPLOAD_CATEGORY_DIRECTORY+"/"+ categoryId +".png");
			imageRepository.save(image);
			category.setImage(image);
			categoryRepository.save(category);
		}

	}

	@Override
	public Image getImagesById(Long categoryId) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.CATEGORY_NOT_FOUND+categoryId));

		return category.getImage();
	}

}
