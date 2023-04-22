package com.kiet.clothesshop.serviceImpl;

import com.kiet.clothesshop.service.IBannerService;
import com.kiet.clothesshop.untils.AppConstant;
import com.kiet.clothesshop.untils.FileUploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kiet.clothesshop.exception.ResourceNotFoundException;
import com.kiet.clothesshop.model.Banner;
import com.kiet.clothesshop.model.Image;
import com.kiet.clothesshop.repository.BannerRepository;
import com.kiet.clothesshop.repository.ImageRepository;


import io.jsonwebtoken.io.IOException;

@Service
public class BannerServiceImpl implements IBannerService {

	@Autowired
	BannerRepository bannerRepository;
	
	@Autowired
	ImageRepository imageRepository;
	
	
	@Override
	public Image getImagesById(Long categoryId) {
		Banner banner = bannerRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.BANNER_IMAGE_NOT_FOUND+categoryId));
		return banner.getImage();
		
	}

	@Override
	public Banner uploadImage(MultipartFile file, Long bannerId) throws IOException, java.io.IOException {
		Banner banner = bannerRepository.findById(bannerId)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.BRAND_NOT_FOUND+bannerId));
		
		Image image;
		if(banner.getImage()!=null) {
			image = banner.getImage();
			uploadImage(file, bannerId, banner, image);
		}else {
			image = new Image();
			uploadImage(file, bannerId, banner, image);
		}
		
		
		return banner;
	}

	private void uploadImage(MultipartFile file, Long bannerId, Banner banner, Image image) throws java.io.IOException {
		if (!file.isEmpty()) {
			FileUploadUtils.saveBannerImage(file, bannerId);
			image.setTitle(bannerId.toString() + ".png");
			image.setPath(AppConstant.UPLOAD_BANNER_DIRECTORY+"/"+ bannerId +".png");
			imageRepository.save(image);
			banner.setImage(image);
			bannerRepository.save(banner);
		}
	}

	@Override
	public Banner addBanner(MultipartFile file) throws java.io.IOException {
		Banner banner = new Banner();
		Banner bannerSaved = bannerRepository.save(banner);
		
		Image image = new Image();
		image.setTitle(bannerSaved.getId()+".png");
		image.setPath(AppConstant.UPLOAD_BANNER_DIRECTORY+"/"+bannerSaved.getId()+".png");

		
		FileUploadUtils.saveBannerImage(file, bannerSaved.getId());
		imageRepository.save(image);
		banner.setImage(image);
		bannerRepository.save(bannerSaved);
		
		return banner;
	}

}
