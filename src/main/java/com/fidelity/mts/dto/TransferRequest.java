package com.fidelity.mts.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class TransferRequest {

	@NotNull(message = "Source account ID is required")
	private Long fromId;

	@NotNull(message = "Destination account ID is required")
	private Long toId;

	@NotNull(message = "Amount is required")
	@Positive(message = "Amount must be greater than zero")
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
