package com.n26.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {

	private BigDecimal amount;
	private LocalDateTime timestamp;

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
}
