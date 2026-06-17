package com.fidelity.mts.entity;

import com.fidelity.mts.enums.AccountStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Version;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 40)
	private String holderName;

	@Column
	private BigDecimal balance = BigDecimal.ZERO;

	@Column
	private AccountStatus status = AccountStatus.ACTIVE;

	@Version
	private Integer version;

	@Column
	private Instant lastUpdated;

	public boolean isActive() {
		return this.status == AccountStatus.ACTIVE;
	}

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public String getHolderName() { return holderName; }
	public void setHolderName(String holderName) { this.holderName = holderName; }

	public BigDecimal getBalance() { return balance; }
	public void setBalance(BigDecimal balance) { this.balance = balance; }

	public AccountStatus getStatus() { return status; }
	public void setStatus(AccountStatus status) { this.status = status; }

	public Integer getVersion() { return version; }
	public void setVersion(Integer version) { this.version = version; }

	public Instant getLastUpdated() { return lastUpdated; }
	public void setLastUpdated(Instant lastUpdated) { this.lastUpdated = lastUpdated; }
}
