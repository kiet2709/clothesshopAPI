package com.kiet.clothesshop.controller;

import com.kiet.clothesshop.exception.ResourceNotFoundException;
import com.kiet.clothesshop.model.Image;
import com.kiet.clothesshop.payload.request.ClothRequest;
import com.kiet.clothesshop.payload.response.BrandResponse;
import com.kiet.clothesshop.payload.response.ClothResponse;
import com.kiet.clothesshop.service.IClothService;
import com.kiet.clothesshop.untils.AppConstant;
import io.jsonwebtoken.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("api/v1/clothes")
@Slf4j
public class ClothController {

	@Autowired
	IClothService clothService;
	
	@GetMapping
	public ResponseEntity<List<ClothResponse>> getAllClothes(){
		List<ClothResponse> cloths = clothService.getAllCloths();
		return new ResponseEntity<>(cloths,HttpStatus.OK);
	}
	
	@GetMapping("/{cloth_id}")
	public ResponseEntity<ClothResponse> getClothById(
			@PathVariable("cloth_id") Long clothId){
		ClothResponse cloth = clothService.getClothById(clothId);
		return new ResponseEntity<>(cloth,HttpStatus.OK);
	}
	
	@GetMapping("/{cloth_id}/brand")
	public ResponseEntity<BrandResponse> getBrandByClothId(
			@PathVariable("cloth_id") Long clothId){
		BrandResponse brand = clothService.getBrandByClothId(clothId);
		return new ResponseEntity<>(brand,HttpStatus.OK);
	}
	
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ClothResponse> addCloth(
			@RequestBody ClothRequest clothRequest){
		ClothResponse cloth = clothService.addCloth(clothRequest);
		return new ResponseEntity<>(cloth,HttpStatus.OK);
	}
	
	@PostMapping("/{cloth_id}/upload-image")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ClothResponse> uploadImage(
			@RequestParam("image") MultipartFile file,
			@PathVariable("cloth_id") Long categoryId) throws IOException, java.io.IOException {
		ClothResponse cloth = clothService.uploadImage(file,categoryId);
		return new ResponseEntity<>(cloth,HttpStatus.OK);
	}
	
	// Get Image
	@GetMapping(value = "/{cloth_id}/images", produces = MediaType.IMAGE_PNG_VALUE)
	public ResponseEntity<InputStreamResource> getImages(@PathVariable("cloth_id") Long clothId) {

		Image image = clothService.getImagesById(clothId);

		try {
			InputStream in = getClass().getResourceAsStream(image.getPath());

			return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(new InputStreamResource(in));
		} catch (Exception e) {
			throw new ResourceNotFoundException(AppConstant.CLOTH_IMAGE_NOT_FOUND);
		}

	}
}
