package com.fidelity.mts.servcie;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fidelity.mts.dto.TransferRequest;
import com.fidelity.mts.dto.TransferResponse;
import com.fidelity.mts.entity.Account;
import com.fidelity.mts.entity.TransactionLog;
import com.fidelity.mts.enums.AccountStatus;
import com.fidelity.mts.enums.TransactionStatus;
import com.fidelity.mts.exception.AccountNotActiveException;
import com.fidelity.mts.exception.AccountNotFoundException;
import com.fidelity.mts.exception.DuplicateTransferException;
import com.fidelity.mts.exception.InsufficientBalanceException;
import com.fidelity.mts.repo.AccountRepo;
import com.fidelity.mts.repo.TransactionLogRepo;

@Service
public class TransferServiceImplementation implements TransferService {

	@Autowired AccountService accountService;
	@Autowired AccountRepo repo;
	@Autowired TransactionLogRepo logrepo;
	@Autowired RewardService rewardService;

	@Override
	@Transactional
	public TransferResponse transferMoney(TransferRequest transferRequest) {

		// FR-05: resolve idempotency key
		String idempotencyKey = transferRequest.getIdempotencyKey();
		if (idempotencyKey == null || idempotencyKey.isBlank()) {
			idempotencyKey = UUID.randomUUID().toString();
		}

		// FR-05: check for duplicate
		Optional<TransactionLog> existing = logrepo.findByIdempotencyKey(idempotencyKey);
		if (existing.isPresent()) {
			TransactionLog prior = existing.get();
			if (prior.getStatus() == TransactionStatus.SUCCESS) {
				return new TransferResponse(
						prior.getId(), prior.getStatus(), "Duplicate — returning previous result",
						prior.getFromAccountId(), prior.getToAccountId(), prior.getAmount());
			}
			throw new DuplicateTransferException();
		}

		// BR-1: source != destination
		if (transferRequest.getFromId() != null
				&& transferRequest.getFromId().equals(transferRequest.getToId())) {
			throw new IllegalArgumentException("Source and destination accounts must be different");
		}

		// BR-6: amount > 0
		if (transferRequest.getAmount() == null
				|| transferRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("Amount must be greater than zero");
		}

		// Prepare log (constructor sets createdOn and a default idempotencyKey)
		TransactionLog log = new TransactionLog(
				transferRequest.getFromId(),
				transferRequest.getToId(),
				transferRequest.getAmount(),
				TransactionStatus.SUCCESS);
		// Override idempotencyKey with the resolved one
		log.setIdempotencyKey(idempotencyKey);

		// BR-2: source must exist
		Account fromAcc;
		try {
			fromAcc = accountService.findById(transferRequest.getFromId());
		} catch (Exception e) {
			recordFailure(log, "ACC-404 Source account not found");
			throw new AccountNotFoundException();
		}

		// BR-4: source must be ACTIVE
		if (fromAcc.getStatus() != AccountStatus.ACTIVE) {
			recordFailure(log, "ACC-403 Source account not active");
			throw new AccountNotActiveException();
		}

		// BR-3: destination must exist
		Account toAcc;
		try {
			toAcc = accountService.findById(transferRequest.getToId());
		} catch (Exception e) {
			recordFailure(log, "ACC-404 Destination account not found");
			throw new AccountNotFoundException();
		}

		// BR-5: destination must be ACTIVE
		if (toAcc.getStatus() != AccountStatus.ACTIVE) {
			recordFailure(log, "ACC-403 Destination account not active");
			throw new AccountNotActiveException();
		}

		// BR-7: sufficient balance
		if (fromAcc.getBalance().compareTo(transferRequest.getAmount()) < 0) {
			recordFailure(log, "TRX-400 Insufficient funds");
			throw new InsufficientBalanceException();
		}

		// BR-9: debit BEFORE credit
		accountService.debit(fromAcc, transferRequest.getAmount());
		accountService.credit(toAcc, transferRequest.getAmount());
		repo.save(fromAcc);
		repo.save(toAcc);

		// BR-10: log the transfer
		log.setStatus(TransactionStatus.SUCCESS);
		logrepo.save(log);

		// Rewards
		if (!rewardService.hasRewardPoints(transferRequest.getFromId())) {
			rewardService.initializeRewardPoints(transferRequest.getFromId());
		}
		if (!rewardService.hasRewardPoints(transferRequest.getToId())) {
			rewardService.initializeRewardPoints(transferRequest.getToId());
		}
		try {
			rewardService.recordReward(
					transferRequest.getFromId(),
					transferRequest.getToId(),
					transferRequest.getAmount(),
					log.getId());
		} catch (Exception e) {
			System.err.println("Failed to record reward points: " + e.getMessage());
		}

		return new TransferResponse(
				log.getId(), log.getStatus(), "Transfer completed",
				log.getFromAccountId(), log.getToAccountId(), log.getAmount());
	}

	private void recordFailure(TransactionLog log, String reason) {
		log.setStatus(TransactionStatus.FAILED);
		log.setFailureReason(reason);
		try { logrepo.save(log); } catch (Exception ignored) { }
	}

	@Override
	public List<TransactionLog> getTransaction(Long id) {
		return logrepo.findByFromAccountIdOrToAccountId(id, id);
	}

	@Override
	public List<TransactionLog> findByFromAccountId(Long id) {
		return logrepo.findByFromAccountId(id);
	}

	@Override
	public List<TransactionLog> findByToAccountId(Long id) {
		return logrepo.findByToAccountId(id);
	}
}
