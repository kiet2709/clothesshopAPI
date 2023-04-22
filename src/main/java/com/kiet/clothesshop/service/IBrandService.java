package com.kiet.clothesshop.service;

import com.kiet.clothesshop.payload.request.BrandRequest;
import com.kiet.clothesshop.payload.response.BrandResponse;
import com.kiet.clothesshop.payload.response.ClothResponse;

import java.util.List;



public interface IBrandService {

	List<BrandResponse> getAllBrands();

	BrandResponse getBrandById(Long brandId);

	List<ClothResponse> getClothesByBrandId(Long brandId);

	BrandResponse addBrand(BrandRequest brandRequest);

}
