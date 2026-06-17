package com.fidelity.mts.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fidelity.mts.dto.TransferRequest;
import com.fidelity.mts.dto.TransferResponse;
import com.fidelity.mts.servcie.TransferService;
import com.fidelity.mts.entity.TransactionLog;

@RestController
@RequestMapping("/api/v1/transfers")
@CrossOrigin(origins = "*")
public class TransferController {

	@Autowired
	TransferService service;

	/**
	 * POST /api/v1/transfers
	 * Initiates a fund transfer between two accounts.
	 */
	@PostMapping
	public ResponseEntity<TransferResponse> transferMoney(@RequestBody TransferRequest transferRequest) {
		return ResponseEntity.status(HttpStatus.OK).body(service.transferMoney(transferRequest));
	}

	/**
	 * GET /api/v1/transfers/{id}/transactions
	 * Returns transaction history for the given account (sent + received).
	 */
	@GetMapping("/{id}/transactions")
	public ResponseEntity<List<TransactionLog>> getTransactions(@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.OK).body(service.getTransaction(id));
	}
}
