package com.kiet.clothesshop.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import com.kiet.clothesshop.exception.ResourceNotFoundException;
import com.kiet.clothesshop.model.Size;
import com.kiet.clothesshop.payload.response.SizeResponse;
import com.kiet.clothesshop.repository.SizeRepository;
import com.kiet.clothesshop.service.ISizeService;
import com.kiet.clothesshop.untils.AppConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SizeServiceImpl implements ISizeService {

	@Autowired
	SizeRepository sizeRepository;

	@Override
	public List<SizeResponse> getAllSizes() {
		List<Size> sizes = sizeRepository.findAll();
		List<SizeResponse> sizeResponses = new ArrayList<>();
		for (Size size: sizes) {
			SizeResponse sizeResponse = new SizeResponse(
					size.getId(),
					size.getName()
			);
			sizeResponses.add(sizeResponse);
		}
		return sizeResponses;
	}

	@Override
	public SizeResponse getSizeById(Long sizeId) {
		Size size = sizeRepository.findById(sizeId)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.SIZE_NOT_FOUND+sizeId));;
		return new SizeResponse(
				size.getId(),
				size.getName()
		);
	}

}
