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
public class AccountServiceImplementation implements AccountService{
	
	@Autowired AccountRepo repo;
	@Autowired RewardService rewardService;
	
	@Override
	public String addAccount(Account act)
	{
		Account savedAccount = repo.save(act);
		
		// Initialize reward points for new account
		try {
			rewardService.initializeRewardPoints(savedAccount.getId());
		} catch (Exception e) {
			System.err.println("Failed to initialize reward points for account " + savedAccount.getId() + ": " + e.getMessage());
		}
		
		return "Added Account with id: "+act.getId();
	}
	
	@Override
	public AccountResponse getDetails(Long id) {
		Account account = repo.findById(id).get();
		if (account == null) throw new AccountNotFoundException();
		AccountResponse accResponseDTO = new AccountResponse(
				account.getId(),
				account.getHolderName(),
				account.getBalance(),
				account.getStatus(),
				account.getVersion(),
				account.getLastUpdated());
		return accResponseDTO;
	}
	
	@Override
	public AccountStatus findByAccountStatus(Long id)  {
		
		Optional<Account> accountStatus = repo.findById(id);
		if(!accountStatus.isPresent()) {
			System.out.println("Account Not Found");
		}
		return accountStatus.get().getStatus();
	}
	
	@Override
	public Account findById(Long id) {
		Optional<Account> foundAcc = repo.findById(id);
		if(foundAcc.isEmpty()) {
			throw new AccountNotFoundException();
		}
		else {
			return foundAcc.get();
		}
	}
	
	@Override
	public BigDecimal getBalance(Long id) {
		Account accountFound = this.findById(id);
		return accountFound.getBalance();		
	}
	
	@Override
	public void credit(Account act, BigDecimal amt) {
		BigDecimal final_amt = act.getBalance().add(amt);
		act.setBalance(final_amt);
		Instant curr_time = Instant.now();
		act.setLastUpdated(curr_time);
	}
	
	@Override
	public void debit(Account act, BigDecimal amt) {
		BigDecimal final_amt = act.getBalance().subtract(amt);
		act.setBalance(final_amt);
		Instant curr_time = Instant.now();
		act.setLastUpdated(curr_time);
	}

}

