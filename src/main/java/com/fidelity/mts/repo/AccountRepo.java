package com.fidelity.mts.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fidelity.mts.entity.Account;

@Repository
public interface AccountRepo extends JpaRepository<Account, Long> {

	// Used by UserDetailsService to load user by username (holderName)
	Optional<Account> findByHolderName(String holderName);

}
