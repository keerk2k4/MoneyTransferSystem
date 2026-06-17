package com.fidelity.mts.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Entity representing a reward transaction.
 * Logs when reward points are earned for a transfer.
 */
@Entity
@Table(name = "reward_transactions")
public class RewardTransaction {

    @Id
    @GeneratedValue
    @UuidGenerator
    @org.hibernate.annotations.JdbcTypeCode(java.sql.Types.BINARY)
    @Column(name = "id", columnDefinition = "BINARY(16)", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "from_account_id", nullable = false)
    private Long fromAccountId;

    @Column(name = "to_account_id", nullable = false)
    private Long toAccountId;

    @Column(name = "transfer_amount", nullable = false)
    private BigDecimal transferAmount;

    @Column(name = "points_earned", nullable = false)
    private Long pointsEarned;

    @org.hibernate.annotations.JdbcTypeCode(java.sql.Types.BINARY)
    @Column(name = "related_transaction_id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID relatedTransactionId;

    @Column(name = "created_on", nullable = false)
    private Instant createdOn;

    // Constructors
    public RewardTransaction() {
        this.createdOn = Instant.now();
    }

    public RewardTransaction(Long fromAccountId, Long toAccountId, BigDecimal transferAmount,
                             Long pointsEarned, UUID relatedTransactionId) {
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.transferAmount = transferAmount;
        this.pointsEarned = pointsEarned;
        this.relatedTransactionId = relatedTransactionId;
        this.createdOn = Instant.now();
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

    @Override
    public String toString() {
        return "RewardTransaction{" +
                "id=" + id +
                ", fromAccountId=" + fromAccountId +
                ", toAccountId=" + toAccountId +
                ", transferAmount=" + transferAmount +
                ", pointsEarned=" + pointsEarned +
                ", relatedTransactionId=" + relatedTransactionId +
                ", createdOn=" + createdOn +
                '}';
    }
}
