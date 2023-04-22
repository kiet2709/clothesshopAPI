package com.kiet.clothesshop.controller;


import com.kiet.clothesshop.exception.ResourceNotFoundException;
import com.kiet.clothesshop.model.Banner;
import com.kiet.clothesshop.model.Image;
import com.kiet.clothesshop.service.IBannerService;
import com.kiet.clothesshop.untils.AppConstant;
import io.jsonwebtoken.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;



@RestController
@RequestMapping("api/v1")
public class DynamicResourceController {

	@Autowired
	IBannerService bannerService;

	// Get Image
	@GetMapping(value = "/banners/{banner_id}/images", produces = MediaType.IMAGE_PNG_VALUE)
	public ResponseEntity<InputStreamResource> getImages(
			@PathVariable("banner_id") Long bannerId) {

		Image image = bannerService.getImagesById(bannerId);

		try {

			System.out.println("Full path: " + image.getPath());
			String url = "/uploads/banners/1.png";
			InputStream in = getClass().getResourceAsStream(image.getPath());
			//return null;
			return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(new InputStreamResource(in));
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResourceNotFoundException(AppConstant.BANNER_IMAGE_NOT_FOUND);
		}

	}

	@PostMapping("/banners/{banner_id}/upload-image")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Banner> uploadImage(
			@RequestParam("image") MultipartFile file,
			@PathVariable("banner_id") Long bannerId) throws IOException, java.io.IOException {
		Banner banner = bannerService.uploadImage(file,bannerId);
		return new ResponseEntity<>(banner,HttpStatus.OK);
	}

	@PostMapping("/banners")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Banner> addBanner(
			@RequestParam("image") MultipartFile file) throws java.io.IOException  {
		Banner banner = bannerService.addBanner(file);
		return new ResponseEntity<>(banner,HttpStatus.OK);
	}
}
