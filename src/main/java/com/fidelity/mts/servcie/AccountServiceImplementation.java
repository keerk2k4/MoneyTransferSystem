package com.fidelity.mts.servcie;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fidelity.mts.dto.AccountResponse;
import com.fidelity.mts.entity.Account;
import com.fidelity.mts.enums.AccountStatus;
import com.fidelity.mts.exception.AccountNotFoundException;
import com.fidelity.mts.repo.AccountRepo;

@Service
public class AccountServiceImplementation implements AccountService {

	@Autowired AccountRepo repo;
	@Autowired RewardService rewardService;

	@Override
	public String addAccount(Account act) {
		Account savedAccount = repo.save(act);
		try {
			rewardService.initializeRewardPoints(savedAccount.getId());
		} catch (Exception e) {
			System.err.println("Failed to initialize reward points for account "
					+ savedAccount.getId() + ": " + e.getMessage());
		}
		return "Added Account with id: " + act.getId();
	}

	@Override
	public AccountResponse getDetails(Long id) {
		// Fix: use orElseThrow instead of .get() to get proper 404
		Account account = repo.findById(id)
				.orElseThrow(() -> new AccountNotFoundException());
		return new AccountResponse(
				account.getId(),
				account.getHolderName(),
				account.getBalance(),
				account.getStatus(),
				account.getVersion(),
				account.getLastUpdated());
	}

	@Override
	public AccountStatus findByAccountStatus(Long id) {
		return repo.findById(id)
				.orElseThrow(() -> new AccountNotFoundException())
				.getStatus();
	}

	@Override
	public Account findById(Long id) {
		return repo.findById(id)
				.orElseThrow(() -> new AccountNotFoundException());
	}

	@Override
	public BigDecimal getBalance(Long id) {
		return this.findById(id).getBalance();
	}

	@Override
	public void credit(Account act, BigDecimal amt) {
		act.setBalance(act.getBalance().add(amt));
		act.setLastUpdated(Instant.now());
	}

	@Override
	public void debit(Account act, BigDecimal amt) {
		act.setBalance(act.getBalance().subtract(amt));
		act.setLastUpdated(Instant.now());
	}
}
