package com.kiet.clothesshop.repository;

import com.kiet.clothesshop.model.Brand;
import com.kiet.clothesshop.model.Cloth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {

	Optional<Brand> findByName(String string);

	Optional<Brand> findByClothes(Cloth cloth);

}
