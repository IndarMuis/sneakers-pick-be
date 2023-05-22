package com.sneakerspick.repository;

import com.sneakerspick.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Long, Role> {
}
