package com.kiet.clothesshop.controller;

import com.kiet.clothesshop.payload.request.BrandRequest;
import com.kiet.clothesshop.payload.response.BrandResponse;
import com.kiet.clothesshop.payload.response.ClothResponse;
import com.kiet.clothesshop.service.IBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/brands")
public class BrandController {

	@Autowired
	IBrandService brandService;
	
	@GetMapping
	public ResponseEntity<List<BrandResponse>> getAllBrands(){
		List<BrandResponse> brands = brandService.getAllBrands();
		return new ResponseEntity<>(brands,HttpStatus.OK);
	}
	
	@GetMapping("/{brand_id}")
	public ResponseEntity<BrandResponse> getBrandById(
			@PathVariable("brand_id") Long brandId){
		BrandResponse brand = brandService.getBrandById(brandId);
		return new ResponseEntity<>(brand,HttpStatus.OK);
	}
	
	@GetMapping("/{brand_id}/clothes")
	public ResponseEntity<List<ClothResponse>> getClothesByBrandId(
			@PathVariable("brand_id") Long brandId){
		List<ClothResponse> clothResponses = brandService.getClothesByBrandId(brandId);
		return new ResponseEntity<>(clothResponses,HttpStatus.OK);
	}
	
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<BrandResponse> addBrand(
			@RequestBody BrandRequest brandRequest){
		BrandResponse brand = brandService.addBrand(brandRequest);
		return new ResponseEntity<>(brand,HttpStatus.OK);
	} 
	
}
