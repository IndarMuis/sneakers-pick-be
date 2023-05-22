package com.sneakerspick.repositories;

import com.sneakerspick.domains.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends JpaRepository<Long, AppUser> {
}
