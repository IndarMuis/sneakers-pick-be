package com.sneakerspick.repository;

import com.sneakerspick.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<Long, AppUser> {
}
