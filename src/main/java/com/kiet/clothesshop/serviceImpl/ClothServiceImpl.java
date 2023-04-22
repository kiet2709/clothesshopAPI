package com.kiet.clothesshop.serviceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.kiet.clothesshop.exception.ResourceNotFoundException;
import com.kiet.clothesshop.model.*;
import com.kiet.clothesshop.payload.request.ClothRequest;
import com.kiet.clothesshop.payload.request.InventoryRequest;
import com.kiet.clothesshop.payload.response.BrandResponse;
import com.kiet.clothesshop.payload.response.ClothResponse;
import com.kiet.clothesshop.repository.*;
import com.kiet.clothesshop.service.IClothService;
import com.kiet.clothesshop.untils.AppConstant;
import com.kiet.clothesshop.untils.FileUploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class ClothServiceImpl implements IClothService {

	@Autowired
	ClothRepository clothRepository;

	@Autowired
	SizeRepository sizeRepository;

	@Autowired
	BrandRepository brandRepository;

	@Autowired
	InventoryRepository inventoryRepository;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	ImageRepository imageRepository;

	@Override
	public List<ClothResponse> getAllCloths() {
		List<Cloth> clothes = clothRepository.findAll();
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
	public ClothResponse getClothById(Long clothId) {
		Cloth cloth = clothRepository.findById(clothId)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.CLOTH_NOT_FOUND+clothId));
		return new ClothResponse(
				cloth.getId(),
				cloth.getName(),
				cloth.getPrice().intValue(),
				cloth.getDescription(),
				cloth.getInventories(),
				cloth.getBrand(),
				cloth.getCategory(),
				cloth.getImage()
		);
	}


	@Override
	public BrandResponse getBrandByClothId(Long clothId) {

		Cloth cloth = clothRepository.findById(clothId)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.CLOTH_NOT_FOUND+clothId));
		Brand brand = brandRepository.findByClothes(cloth)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.BRAND_NOT_FOUND+ clothId));
		return new BrandResponse(
				brand.getId(),
				brand.getName()
		);
	}

	@Override
	public ClothResponse addCloth(ClothRequest clothRequest) {

		Brand brand = brandRepository.findById(clothRequest.getBrandId())
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.BRAND_NOT_FOUND+clothRequest.getBrandId()));
		Category category = categoryRepository.findById(clothRequest.getCategoryId())
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.CATEGORY_NOT_FOUND+clothRequest.getCategoryId()));

		Cloth cloth = new Cloth();
		cloth.setBrand(brand);
		cloth.setCategory(category);
		cloth.setDescription(clothRequest.getDescription());
		cloth.setName(clothRequest.getName());
		cloth.setPrice(clothRequest.getPrice());

		Cloth clothSaved = clothRepository.save(cloth);
		ClothResponse clothResponse = new ClothResponse();
//				mapper.map(clothSaved, ClothResponse.class);
		//map clothSaved with clothResponse
		clothResponse.setId(clothSaved.getId());
		clothResponse.setName(clothSaved.getName());
		clothResponse.setPrice(clothSaved.getPrice().intValue());
		clothResponse.setBrand(clothSaved.getBrand());
		clothResponse.setDescription(clothSaved.getDescription());
		clothResponse.setCategory(clothSaved.getCategory());


		for (InventoryRequest inventory : clothRequest.getInventory()) {
			Inventory newInventory = new Inventory();
			newInventory.setQuantity(inventory.getQuantity());

			Size size = sizeRepository.findById(inventory.getSizeId())
					.orElseThrow(() -> new ResourceNotFoundException(AppConstant.SIZE_NOT_FOUND+inventory.getSizeId()));
			newInventory.setSize(size);
			newInventory.setCloth(clothSaved);
			inventoryRepository.save(newInventory);

			clothResponse.addInventory(newInventory);
		}


		return clothResponse;
	}

	@Override
	public ClothResponse uploadImage(MultipartFile file, Long clothId) throws IOException {
		Cloth cloth = clothRepository.findById(clothId)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.CLOTH_NOT_FOUND+clothId));

		Image image;
		if(cloth.getImage()!=null) {
			image = cloth.getImage();
			uploadImage(file, clothId, cloth, image);
		}else {
			image = new Image();
			uploadImage(file, clothId, cloth, image);
		}


		return new ClothResponse(
				cloth.getId(),
				cloth.getName(),
				cloth.getPrice().intValue(),
				cloth.getDescription(),
				cloth.getInventories(),
				cloth.getBrand(),
				cloth.getCategory(),
				cloth.getImage()
		);
	}

	private void uploadImage(MultipartFile file, Long clothId, Cloth cloth, Image image) throws IOException {
		if (!file.isEmpty()) {
			FileUploadUtils.saveClothImage(file, clothId);
			image.setTitle(clothId.toString() + ".png");
			image.setPath(AppConstant.UPLOAD_CLOTH_DIRECTORY+"/"+ clothId +".png");
			imageRepository.save(image);
			cloth.setImage(image);
			clothRepository.save(cloth);
		}
	}

	@Override
	public Image getImagesById(Long clothId) {
		Cloth cloth = clothRepository.findById(clothId)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.CLOTH_NOT_FOUND+clothId));

		return cloth.getImage();
	}

}
