package com.fidelity.mts.servcie;

import com.fidelity.mts.dto.RewardPointsResponse;
import com.fidelity.mts.dto.RewardTransactionResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing reward points and transactions.
 */
public interface RewardService {
    
    /**
     * Get reward points for an account.
     * @param accountId The account ID
     * @return RewardPointsResponse containing current points
     */
    RewardPointsResponse getRewardPoints(Long accountId);
    
    /**
     * Record a reward for a successful transfer.
     * @param fromAccountId Source account ID
     * @param toAccountId Destination account ID
     * @param transferAmount Transfer amount
     * @param transactionId Related transaction ID
     * @return RewardTransactionResponse if reward was earned, null if not eligible
     */
    RewardTransactionResponse recordReward(Long fromAccountId, Long toAccountId, 
                                            BigDecimal transferAmount, UUID transactionId);
    
    /**
     * Get reward transaction history for an account (as sender).
     * @param accountId The account ID
     * @return List of reward transaction responses
     */
    List<RewardTransactionResponse> getRewardHistory(Long accountId);
    
    /**
     * Initialize reward points for a new account.
     * @param accountId The account ID
     */
    void initializeRewardPoints(Long accountId);
    
    /**
     * Check if an account has initialized reward points.
     * @param accountId The account ID
     * @return true if reward points exist, false otherwise
     */
    boolean hasRewardPoints(Long accountId);
}
