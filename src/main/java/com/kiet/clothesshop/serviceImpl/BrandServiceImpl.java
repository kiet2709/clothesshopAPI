package com.kiet.clothesshop.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import com.kiet.clothesshop.exception.ResourceNotFoundException;
import com.kiet.clothesshop.model.Brand;
import com.kiet.clothesshop.model.Cloth;
import com.kiet.clothesshop.payload.request.BrandRequest;
import com.kiet.clothesshop.payload.response.BrandResponse;
import com.kiet.clothesshop.payload.response.ClothResponse;
import com.kiet.clothesshop.repository.BrandRepository;
import com.kiet.clothesshop.repository.ClothRepository;
import com.kiet.clothesshop.untils.AppConstant;
import com.kiet.clothesshop.service.IBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class BrandServiceImpl implements IBrandService{

	@Autowired
	BrandRepository brandRepository;

	@Autowired
	ClothRepository clothRepository;


	@Override
	public List<BrandResponse> getAllBrands() {
		List<Brand> brands =  brandRepository.findAll();
		List<BrandResponse> brandResponses = new ArrayList<>();
		for (int i = 0; i < brands.size(); i++) {
			BrandResponse brandResponse = new BrandResponse();
			brandResponse.setId(brands.get(i).getId());
			brandResponse.setName(brands.get(i).getName());
			brandResponses.add(brandResponse);
		}
		return brandResponses;
	}

	@Override
	public BrandResponse getBrandById(Long brandId) {
		Brand brand = brandRepository.findById(brandId)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.BRAND_NOT_FOUND+brandId));
		return new BrandResponse(brand.getId(), brand.getName());

	}

	@Override
	public List<ClothResponse> getClothesByBrandId(Long brandId) {
		Brand brand = brandRepository.findById(brandId)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.BRAND_NOT_FOUND+brandId));
		List<Cloth> clothes = clothRepository.findByBrand(brand);
		List<ClothResponse> clothResponses =new ArrayList<>();
		for (Cloth clothe : clothes) {
			ClothResponse clothResponse = new ClothResponse();
			clothResponse.setId(clothe.getId());
			clothResponse.setName(clothe.getName());
			clothResponse.setPrice(clothe.getPrice().intValue());
			clothResponse.setDescription(clothe.getDescription());
			clothResponse.setInventories(clothe.getInventories());
			clothResponse.setBrand(clothe.getBrand());
			clothResponse.setCategory(clothe.getCategory());
			clothResponse.setImage(clothe.getImage());
			clothResponses.add(clothResponse);
		}
		return clothResponses;
	}

	@Override
	public BrandResponse addBrand(BrandRequest brandRequest) {
		Brand brand = new Brand();
		brand.setName(brandRequest.getName());
		brand = brandRepository.save(brand);
		return new BrandResponse(brand.getId(), brand.getName());
	}

}
