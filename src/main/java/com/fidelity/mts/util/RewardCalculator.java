package com.fidelity.mts.util;

import java.math.BigDecimal;

/**
 * Utility class for calculating reward points based on transfer rules.
 * 
 * Reward Eligibility Rules:
 * 1. Transaction status is success
 * 2. Transaction amount is greater than Rs 100
 * 3. Sender and receiver are different users
 * 4. Transaction is not self-transfer
 * 
 * Calculation Logic:
 * - 1 reward point per Rs 100 transferred
 * - Points are rounded down
 * - Example: Rs 250 → 2 points, Rs 99 → 0 points
 */
public class RewardCalculator {

    private static final BigDecimal MINIMUM_ELIGIBLE_AMOUNT = BigDecimal.valueOf(100);
    private static final BigDecimal POINTS_THRESHOLD = BigDecimal.valueOf(100);

    /**
     * Calculate reward points for a successful transfer.
     * 
     * @param fromAccountId Source account ID
     * @param toAccountId Destination account ID
     * @param transferAmount Transfer amount
     * @return Reward points earned (0 if not eligible)
     */
    public static Long calculateRewardPoints(Long fromAccountId, Long toAccountId, BigDecimal transferAmount) {
        // Rule 1: Sender and receiver must be different (not self-transfer)
        if (fromAccountId.equals(toAccountId)) {
            return 0L;
        }

        // Rule 2: Transfer amount must be greater than Rs 100
        if (transferAmount.compareTo(MINIMUM_ELIGIBLE_AMOUNT) <= 0) {
            return 0L;
        }

        // Calculation: 1 point per Rs 100, rounded down
        long points = transferAmount.divide(POINTS_THRESHOLD, BigDecimal.ROUND_DOWN).longValue();
        
        return points;
    }

    /**
     * Check if transfer is eligible for rewards.
     * 
     * @param fromAccountId Source account ID
     * @param toAccountId Destination account ID
     * @param transferAmount Transfer amount
     * @return true if eligible for rewards, false otherwise
     */
    public static boolean isEligibleForRewards(Long fromAccountId, Long toAccountId, BigDecimal transferAmount) {
        return !fromAccountId.equals(toAccountId) && transferAmount.compareTo(MINIMUM_ELIGIBLE_AMOUNT) > 0;
    }
}
