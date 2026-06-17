package com.fidelity.mts.entity;

import com.fidelity.mts.enums.TransactionStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
public class TransactionLog {
	@Id
	@GeneratedValue
	@UuidGenerator
	@Column(columnDefinition = "BINARY(16)", nullable = false, updatable = false)
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.BINARY)
	private UUID id;

    @Column(nullable = false)
    private Long fromAccountId;

    @Column(nullable = false)
    private Long toAccountId;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    
    @Column(nullable = false)
    private TransactionStatus status;

    private String failureReason;

    @Column(nullable = false, updatable = false)
    private Instant createdOn;
    
    @Column(nullable = false, unique = true, updatable = false)
    private String idempotencyKey;

    public UUID getId() {
		return id;
	}


	public void setId(UUID id) {
		this.id = id;
	}

	public TransactionLog() {
    // required by JPA/Hibernate
}



	public TransactionLog(Long fromAccountId, Long toAccountId, BigDecimal amount, TransactionStatus status) {
		super();
		this.fromAccountId = fromAccountId;
		this.toAccountId = toAccountId;
		this.amount = amount;
		this.status = status;
		this.failureReason = "";
		this.createdOn = Instant.now();
		this.idempotencyKey = UUID.randomUUID().toString();;
	}


	public String getIdempotencyKey() {
		return idempotencyKey;
	}
	public void setIdempotencyKey(String idempotencyKey) {
		this.idempotencyKey = idempotencyKey;
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
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public TransactionStatus getStatus() {
		return status;
	}
	public void setStatus(TransactionStatus status) {
		this.status = status;
	}
	public String getFailureReason() {
		return failureReason;
	}
	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}
	public Instant getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn() {
		this.createdOn = Instant.now();
	}
	
}