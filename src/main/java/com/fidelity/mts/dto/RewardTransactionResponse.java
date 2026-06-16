package com.fidelity.mts.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * DTO for Reward Transaction response
 */
public class RewardTransactionResponse {
    private UUID id;
    private Long fromAccountId;
    private Long toAccountId;
    private BigDecimal transferAmount;
    private Long pointsEarned;
    private UUID relatedTransactionId;
    private Instant createdOn;

    // Constructors
    public RewardTransactionResponse() {}

    public RewardTransactionResponse(UUID id, Long fromAccountId, Long toAccountId,
                                     BigDecimal transferAmount, Long pointsEarned,
                                     UUID relatedTransactionId, Instant createdOn) {
        this.id = id;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.transferAmount = transferAmount;
        this.pointsEarned = pointsEarned;
        this.relatedTransactionId = relatedTransactionId;
        this.createdOn = createdOn;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(Long fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public Long getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(Long toAccountId) {
        this.toAccountId = toAccountId;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public Long getPointsEarned() {
        return pointsEarned;
    }

    public void setPointsEarned(Long pointsEarned) {
        this.pointsEarned = pointsEarned;
    }

    public UUID getRelatedTransactionId() {
        return relatedTransactionId;
    }

    public void setRelatedTransactionId(UUID relatedTransactionId) {
        this.relatedTransactionId = relatedTransactionId;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }
}
