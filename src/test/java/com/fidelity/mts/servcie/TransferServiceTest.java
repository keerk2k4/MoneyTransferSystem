package com.fidelity.mts.servcie;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fidelity.mts.dto.TransferRequest;
import com.fidelity.mts.dto.TransferResponse;
import com.fidelity.mts.entity.Account;
import com.fidelity.mts.entity.TransactionLog;
import com.fidelity.mts.enums.AccountStatus;
import com.fidelity.mts.enums.TransactionStatus;
import com.fidelity.mts.exception.AccountNotActiveException;
import com.fidelity.mts.exception.AccountNotFoundException;
import com.fidelity.mts.exception.InsufficientBalanceException;
import com.fidelity.mts.repo.AccountRepo;
import com.fidelity.mts.repo.TransactionLogRepo;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

	@Mock private AccountService accountService;
	@Mock private AccountRepo repo;
	@Mock private TransactionLogRepo logrepo;
	@Mock private RewardService rewardService;

	@InjectMocks
	private TransferServiceImplementation transferService;

	private Account fromAccount;
	private Account toAccount;
	private TransferRequest request;

	@BeforeEach
	void setUp() {
		fromAccount = new Account();
		fromAccount.setId(1L);
		fromAccount.setHolderName("Alice");
		fromAccount.setBalance(new BigDecimal("5000.00"));
		fromAccount.setStatus(AccountStatus.ACTIVE);

		toAccount = new Account();
		toAccount.setId(2L);
		toAccount.setHolderName("Bob");
		toAccount.setBalance(new BigDecimal("1000.00"));
		toAccount.setStatus(AccountStatus.ACTIVE);

		request = new TransferRequest();
		request.setFromId(1L);
		request.setToId(2L);
		request.setAmount(new BigDecimal("500.00"));
		request.setIdempotencyKey(UUID.randomUUID().toString());
	}

	@Test
	@DisplayName("FR-01: Successful fund transfer")
	void testSuccessfulTransfer() {
		when(logrepo.findByIdempotencyKey(any())).thenReturn(Optional.empty());
		when(accountService.findById(1L)).thenReturn(fromAccount);
		when(accountService.findById(2L)).thenReturn(toAccount);
		when(logrepo.save(any())).thenAnswer(i -> i.getArgument(0));
		when(rewardService.hasRewardPoints(anyLong())).thenReturn(true);

		TransferResponse resp = transferService.transferMoney(request);

		assertEquals(TransactionStatus.SUCCESS, resp.status());
		assertEquals(1L, resp.debitedFrom());
		assertEquals(2L, resp.creditedTo());
		assertEquals(new BigDecimal("500.00"), resp.amount());
		verify(accountService).debit(fromAccount, new BigDecimal("500.00"));
		verify(accountService).credit(toAccount, new BigDecimal("500.00"));
	}

	@Test
	@DisplayName("BR-1: Source and destination must be different")
	void testSameAccountTransfer() {
		request.setToId(1L);
		when(logrepo.findByIdempotencyKey(any())).thenReturn(Optional.empty());

		assertThrows(IllegalArgumentException.class,
				() -> transferService.transferMoney(request));
	}

	@Test
	@DisplayName("BR-2: Source account must exist")
	void testSourceAccountNotFound() {
		when(logrepo.findByIdempotencyKey(any())).thenReturn(Optional.empty());
		when(accountService.findById(1L)).thenThrow(new AccountNotFoundException());

		assertThrows(AccountNotFoundException.class,
				() -> transferService.transferMoney(request));
	}

	@Test
	@DisplayName("BR-4: Source account must be ACTIVE")
	void testSourceAccountNotActive() {
		fromAccount.setStatus(AccountStatus.INACTIVE);
		when(logrepo.findByIdempotencyKey(any())).thenReturn(Optional.empty());
		when(accountService.findById(1L)).thenReturn(fromAccount);

		assertThrows(AccountNotActiveException.class,
				() -> transferService.transferMoney(request));
	}

	@Test
	@DisplayName("BR-6: Amount must be greater than zero")
	void testZeroAmount() {
		request.setAmount(BigDecimal.ZERO);
		when(logrepo.findByIdempotencyKey(any())).thenReturn(Optional.empty());

		assertThrows(IllegalArgumentException.class,
				() -> transferService.transferMoney(request));
	}

	@Test
	@DisplayName("BR-7: Insufficient balance")
	void testInsufficientBalance() {
		request.setAmount(new BigDecimal("99999.00"));
		when(logrepo.findByIdempotencyKey(any())).thenReturn(Optional.empty());
		when(accountService.findById(1L)).thenReturn(fromAccount);
		when(accountService.findById(2L)).thenReturn(toAccount);

		assertThrows(InsufficientBalanceException.class,
				() -> transferService.transferMoney(request));
	}

	@Test
	@DisplayName("FR-05: Idempotency — duplicate returns previous result")
	void testDuplicateReturnsExisting() {
		TransactionLog prior = new TransactionLog(1L, 2L, new BigDecimal("500"), TransactionStatus.SUCCESS);
		when(logrepo.findByIdempotencyKey(any())).thenReturn(Optional.of(prior));

		TransferResponse resp = transferService.transferMoney(request);

		assertEquals(TransactionStatus.SUCCESS, resp.status());
		verify(accountService, never()).debit(any(), any());
	}
}
