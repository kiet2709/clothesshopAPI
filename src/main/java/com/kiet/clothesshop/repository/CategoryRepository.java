package com.kiet.clothesshop.repository;

import com.kiet.clothesshop.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	Optional<Category> findByName(String string);


}
