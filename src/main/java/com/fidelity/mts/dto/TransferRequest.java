package com.fidelity.mts.dto;

import java.math.BigDecimal;

public class TransferRequest {

	private Long fromId;
	private Long toId;
	private BigDecimal amount;
	private String idempotencyKey;

	public Long getFromId() { return fromId; }
	public void setFromId(Long fromId) { this.fromId = fromId; }

	public Long getToId() { return toId; }
	public void setToId(Long toId) { this.toId = toId; }

	public BigDecimal getAmount() { return amount; }
	public void setAmount(BigDecimal amount) { this.amount = amount; }

	public String getIdempotencyKey() { return idempotencyKey; }
	public void setIdempotencyKey(String idempotencyKey) { this.idempotencyKey = idempotencyKey; }
}
