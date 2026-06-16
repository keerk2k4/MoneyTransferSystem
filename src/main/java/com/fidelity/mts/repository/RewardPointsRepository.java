package com.fidelity.mts.repository;

import com.fidelity.mts.entity.RewardPoints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RewardPointsRepository extends JpaRepository<RewardPoints, Long> {
    
    /**
     * Find reward points by account ID.
     * @param accountId The account ID
     * @return Optional containing RewardPoints if found
     */
    Optional<RewardPoints> findByAccountId(Long accountId);
}
