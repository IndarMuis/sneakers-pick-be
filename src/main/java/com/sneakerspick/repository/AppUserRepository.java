package com.sneakerspick.repository;

import com.sneakerspick.Model.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Integer> {

	Optional<AppUser> findDistinctTopByUsername(String username);
	boolean existsAppUserByUsername(String username);

}
