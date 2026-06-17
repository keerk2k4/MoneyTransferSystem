package com.fidelity.mts.controller;

import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fidelity.mts.dto.AccountResponse;
import com.fidelity.mts.servcie.AccountService;

@RestController
@RequestMapping("/api/v1/account")
@CrossOrigin(origins = "*")
public class AccountController {

	@Autowired
	AccountService service;

	@GetMapping("{id}")
	public ResponseEntity<AccountResponse> getDetails(@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.OK).body(service.getDetails(id));
	}

	@GetMapping("{id}/balance")
	public ResponseEntity<BigDecimal> getBalance(@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.OK).body(service.getBalance(id));
	}
}
