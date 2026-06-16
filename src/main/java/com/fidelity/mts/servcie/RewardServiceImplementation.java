package com.fidelity.mts.servcie;

import com.fidelity.mts.dto.RewardPointsResponse;
import com.fidelity.mts.dto.RewardTransactionResponse;
import com.fidelity.mts.entity.RewardPoints;
import com.fidelity.mts.entity.RewardTransaction;
import com.fidelity.mts.repository.RewardPointsRepository;
import com.fidelity.mts.repository.RewardTransactionRepository;
import com.fidelity.mts.util.RewardCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service implementation for managing reward points and transactions.
 */
@Service
public class RewardServiceImplementation implements RewardService {

    @Autowired
    private RewardPointsRepository rewardPointsRepository;

    @Autowired
    private RewardTransactionRepository rewardTransactionRepository;

    @Override
    @Transactional(readOnly = true)
    public RewardPointsResponse getRewardPoints(Long accountId) {
        RewardPoints rewardPoints = rewardPointsRepository.findByAccountId(accountId)
                .orElse(null);
        
        if (rewardPoints == null) {
            return new RewardPointsResponse(accountId, 0L, null, Instant.now());
        }
        
        return new RewardPointsResponse(
                rewardPoints.getAccountId(),
                rewardPoints.getTotalPoints(),
                rewardPoints.getLastEarnedOn(),
                rewardPoints.getUpdatedOn()
        );
    }

    @Override
    @Transactional
    public RewardTransactionResponse recordReward(Long fromAccountId, Long toAccountId,
                                                   BigDecimal transferAmount, UUID transactionId) {
        // Calculate reward points
        Long pointsEarned = RewardCalculator.calculateRewardPoints(fromAccountId, toAccountId, transferAmount);
        
        // If not eligible, return null
        if (pointsEarned == 0) {
            return null;
        }
        
        // Get or create reward points for the sender
        RewardPoints rewardPoints = rewardPointsRepository.findByAccountId(fromAccountId)
                .orElse(new RewardPoints(fromAccountId));
        
        // Add points
        rewardPoints.addPoints(pointsEarned);
        rewardPointsRepository.save(rewardPoints);
        
        // Create reward transaction record
        RewardTransaction rewardTransaction = new RewardTransaction(
                fromAccountId,
                toAccountId,
                transferAmount,
                pointsEarned,
                transactionId
        );
        
        RewardTransaction savedTransaction = rewardTransactionRepository.save(rewardTransaction);
        
        // Return response
        return new RewardTransactionResponse(
                savedTransaction.getId(),
                savedTransaction.getFromAccountId(),
                savedTransaction.getToAccountId(),
                savedTransaction.getTransferAmount(),
                savedTransaction.getPointsEarned(),
                savedTransaction.getRelatedTransactionId(),
                savedTransaction.getCreatedOn()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<RewardTransactionResponse> getRewardHistory(Long accountId) {
        List<RewardTransaction> transactions = rewardTransactionRepository
                .findByFromAccountIdOrderByCreatedOnDesc(accountId);
        
        return transactions.stream()
                .map(t -> new RewardTransactionResponse(
                        t.getId(),
                        t.getFromAccountId(),
                        t.getToAccountId(),
                        t.getTransferAmount(),
                        t.getPointsEarned(),
                        t.getRelatedTransactionId(),
                        t.getCreatedOn()
                ))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void initializeRewardPoints(Long accountId) {
        if (!rewardPointsRepository.findByAccountId(accountId).isPresent()) {
            RewardPoints rewardPoints = new RewardPoints(accountId);
            rewardPointsRepository.save(rewardPoints);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasRewardPoints(Long accountId) {
        return rewardPointsRepository.findByAccountId(accountId).isPresent();
    }
}
