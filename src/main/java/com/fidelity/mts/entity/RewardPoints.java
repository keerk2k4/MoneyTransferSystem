package com.fidelity.mts.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * Entity representing reward points for an account.
 * Tracks total points earned and last update timestamp.
 */
@Entity
@Table(name = "reward_points")
public class RewardPoints {

    @Id
    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "total_points", nullable = false)
    private Long totalPoints = 0L;

    @Column(name = "last_earned_on")
    private Instant lastEarnedOn;

    @Column(name = "created_on", nullable = false)
    private Instant createdOn;

    @Column(name = "updated_on", nullable = false)
    private Instant updatedOn;

    // Constructors
    public RewardPoints() {
        this.createdOn = Instant.now();
        this.updatedOn = Instant.now();
    }

    public RewardPoints(Long accountId) {
        this.accountId = accountId;
        this.totalPoints = 0L;
        this.createdOn = Instant.now();
        this.updatedOn = Instant.now();
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

    public void addPoints(Long points) {
        this.totalPoints += points;
        this.lastEarnedOn = Instant.now();
        this.updatedOn = Instant.now();
    }

    public Instant getLastEarnedOn() {
        return lastEarnedOn;
    }

    public void setLastEarnedOn(Instant lastEarnedOn) {
        this.lastEarnedOn = lastEarnedOn;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public Instant getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Instant updatedOn) {
        this.updatedOn = updatedOn;
    }

    @Override
    public String toString() {
        return "RewardPoints{" +
                "accountId=" + accountId +
                ", totalPoints=" + totalPoints +
                ", lastEarnedOn=" + lastEarnedOn +
                ", createdOn=" + createdOn +
                ", updatedOn=" + updatedOn +
                '}';
    }
}
