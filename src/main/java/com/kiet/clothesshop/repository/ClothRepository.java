package com.kiet.clothesshop.repository;

import com.kiet.clothesshop.model.Brand;
import com.kiet.clothesshop.model.Category;
import com.kiet.clothesshop.model.Cloth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ClothRepository extends JpaRepository<Cloth, Long> {

	List<Cloth> findByCategory(Category category);

	List<Cloth> findByBrand(Brand brand);

}
