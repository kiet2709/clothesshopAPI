package com.kiet.clothesshop.repository;


import com.kiet.clothesshop.model.Cloth;
import com.kiet.clothesshop.model.Inventory;
import com.kiet.clothesshop.model.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

	List<Size> findSizeByCloth(Cloth cloth);

	Optional<Inventory> findByClothAndSize(Cloth cloth, Size choice_size);

}
