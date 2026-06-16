package com.fidelity.mts.repository;

import com.fidelity.mts.entity.RewardTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RewardTransactionRepository extends JpaRepository<RewardTransaction, UUID> {
    
    /**
     * Find all reward transactions for a specific account (as sender).
     * @param fromAccountId The sender account ID
     * @return List of reward transactions
     */
    List<RewardTransaction> findByFromAccountIdOrderByCreatedOnDesc(Long fromAccountId);
    
    /**
     * Find all reward transactions for a specific account (as receiver).
     * @param toAccountId The receiver account ID
     * @return List of reward transactions
     */
    List<RewardTransaction> findByToAccountIdOrderByCreatedOnDesc(Long toAccountId);
    
    /**
     * Find reward transaction by related transaction ID.
     * @param relatedTransactionId The related TransactionLog ID
     * @return List of matching reward transactions
     */
    List<RewardTransaction> findByRelatedTransactionId(UUID relatedTransactionId);
}
