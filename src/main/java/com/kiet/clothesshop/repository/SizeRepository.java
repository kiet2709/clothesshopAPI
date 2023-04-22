package com.kiet.clothesshop.repository;

import com.kiet.clothesshop.model.Size;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SizeRepository extends JpaRepository<Size, Long> {

	Size findByName(String string);


}
