package com.fidelity.mts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fidelity.mts.dto.ErrorResponse;

@ControllerAdvice
public class AccGlobalExceptionHandler {

	/** ACC-404 — Account Not Found (HTTP 404) */
	@ExceptionHandler(AccountNotFoundException.class)
	public ResponseEntity<ErrorResponse> accNotFound(AccountNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new ErrorResponse("ACC-404", "Account Not Found"));
	}

	/** ACC-403 — Account Not Active (HTTP 403) */
	@ExceptionHandler(AccountNotActiveException.class)
	public ResponseEntity<ErrorResponse> handleNotActive(AccountNotActiveException ex) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body(new ErrorResponse("ACC-403", "Account Not Active"));
	}

	/** TRX-400 — Insufficient Funds (HTTP 400) */
	@ExceptionHandler(InsufficientBalanceException.class)
	public ResponseEntity<ErrorResponse> handleInsufficient(InsufficientBalanceException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse("TRX-400", "Insufficient Funds"));
	}

	/** TRX-409 — Duplicate Transfer / Idempotency violation (HTTP 409) */
	@ExceptionHandler(DuplicateTransferException.class)
	public ResponseEntity<ErrorResponse> handleDuplicate(DuplicateTransferException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT)
				.body(new ErrorResponse("TRX-409", "Duplicate transfer request"));
	}

	/** VAL-422 — Invalid Input from business-rule checks (HTTP 422) */
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArg(IllegalArgumentException ex) {
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.body(new ErrorResponse("VAL-422", ex.getMessage()));
	}

	/** VAL-422 — Bean Validation failures from @Valid (HTTP 422) */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
		String msg = ex.getBindingResult().getFieldErrors().stream()
				.map(e -> e.getField() + ": " + e.getDefaultMessage())
				.reduce((a, b) -> a + "; " + b)
				.orElse("Invalid input");
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.body(new ErrorResponse("VAL-422", msg));
	}

	/** NFR-02 — Optimistic lock conflict (HTTP 409) */
	@ExceptionHandler(ObjectOptimisticLockingFailureException.class)
	public ResponseEntity<ErrorResponse> handleOptimisticLock(ObjectOptimisticLockingFailureException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT)
				.body(new ErrorResponse("TRX-409", "Concurrent modification detected. Please retry."));
	}
}
