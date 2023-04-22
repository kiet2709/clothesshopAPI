package com.kiet.clothesshop.repository;


import com.kiet.clothesshop.model.user.Role;
import com.kiet.clothesshop.model.user.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long>{

	Role findByName(RoleName name);

}
