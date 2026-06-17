package com.fidelity.mts.servcie;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fidelity.mts.dto.AccountResponse;
import com.fidelity.mts.entity.Account;
import com.fidelity.mts.enums.AccountStatus;
import com.fidelity.mts.exception.AccountNotFoundException;
import com.fidelity.mts.repo.AccountRepo;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

	@Mock private AccountRepo repo;
	@Mock private RewardService rewardService;

	@InjectMocks
	private AccountServiceImplementation accountService;

	private Account testAccount;

	@BeforeEach
	void setUp() {
		testAccount = new Account();
		testAccount.setId(1L);
		testAccount.setHolderName("Alice");
		testAccount.setBalance(new BigDecimal("5000.00"));
		testAccount.setStatus(AccountStatus.ACTIVE);
		testAccount.setVersion(0);
		testAccount.setLastUpdated(Instant.now());
	}

	@Test
	@DisplayName("FR-06: Get account details")
	void testGetDetails() {
		when(repo.findById(1L)).thenReturn(Optional.of(testAccount));
		AccountResponse resp = accountService.getDetails(1L);
		assertEquals(1L, resp.id());
		assertEquals("Alice", resp.holderName());
		assertEquals(new BigDecimal("5000.00"), resp.balance());
	}

	@Test
	@DisplayName("FR-06: Get details for non-existent account throws ACC-404")
	void testGetDetailsNotFound() {
		when(repo.findById(99L)).thenReturn(Optional.empty());
		assertThrows(AccountNotFoundException.class,
				() -> accountService.getDetails(99L));
	}

	@Test
	@DisplayName("FR-06: Get balance")
	void testGetBalance() {
		when(repo.findById(1L)).thenReturn(Optional.of(testAccount));
		BigDecimal balance = accountService.getBalance(1L);
		assertEquals(new BigDecimal("5000.00"), balance);
	}

	@Test
	@DisplayName("BR-9: Debit reduces balance")
	void testDebit() {
		accountService.debit(testAccount, new BigDecimal("200.00"));
		assertEquals(new BigDecimal("4800.00"), testAccount.getBalance());
		assertNotNull(testAccount.getLastUpdated());
	}

	@Test
	@DisplayName("BR-9: Credit increases balance")
	void testCredit() {
		accountService.credit(testAccount, new BigDecimal("300.00"));
		assertEquals(new BigDecimal("5300.00"), testAccount.getBalance());
		assertNotNull(testAccount.getLastUpdated());
	}
}
