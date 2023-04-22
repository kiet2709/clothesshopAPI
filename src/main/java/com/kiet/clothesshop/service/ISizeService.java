package com.kiet.clothesshop.service;

import com.kiet.clothesshop.payload.response.SizeResponse;

import java.util.List;


public interface ISizeService {

	List<SizeResponse> getAllSizes();

	SizeResponse getSizeById(Long sizeId);

}
