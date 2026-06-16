package com.fidelity.mts.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * DTO for Reward Points response
 */
public class RewardPointsResponse {
    private Long accountId;
    private Long totalPoints;
    private Instant lastEarnedOn;
    private Instant updatedOn;

    // Constructors
    public RewardPointsResponse() {}

    public RewardPointsResponse(Long accountId, Long totalPoints, Instant lastEarnedOn, Instant updatedOn) {
        this.accountId = accountId;
        this.totalPoints = totalPoints;
        this.lastEarnedOn = lastEarnedOn;
        this.updatedOn = updatedOn;
    }

    // Getters and Setters
    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Long totalPoints) {
        this.totalPoints = totalPoints;
    }

    public Instant getLastEarnedOn() {
        return lastEarnedOn;
    }

    public void setLastEarnedOn(Instant lastEarnedOn) {
        this.lastEarnedOn = lastEarnedOn;
    }

    public Instant getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Instant updatedOn) {
        this.updatedOn = updatedOn;
    }
}
