package com.kiet.clothesshop.controller;


import com.kiet.clothesshop.payload.response.SizeResponse;
import com.kiet.clothesshop.service.ISizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/sizes")
public class SizeController {

	@Autowired
	ISizeService sizeService;
	
	@GetMapping
	public ResponseEntity<List<SizeResponse>> getAllSizes(){
		List<SizeResponse> sizes = sizeService.getAllSizes();
		return new ResponseEntity<>(sizes,HttpStatus.OK);
	}
	
	@GetMapping("/{size_id}")
	public ResponseEntity<SizeResponse> getSizeById(
			@PathVariable("size_id") Long sizeId){
		SizeResponse size = sizeService.getSizeById(sizeId);
		return new ResponseEntity<>(size,HttpStatus.OK);
	}
}
