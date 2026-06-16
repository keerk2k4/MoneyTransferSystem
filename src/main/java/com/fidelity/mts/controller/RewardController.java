package com.fidelity.mts.controller;

import com.fidelity.mts.dto.RewardPointsResponse;
import com.fidelity.mts.dto.RewardTransactionResponse;
import com.fidelity.mts.servcie.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for reward management endpoints.
 */
@RestController
@RequestMapping("/api/v1/rewards")
@CrossOrigin(origins = "*")
public class RewardController {

    @Autowired
    private RewardService rewardService;

    /**
     * Get reward points for an account.
     * @param accountId The account ID
     * @return RewardPointsResponse
     */
    @GetMapping("/points/{accountId}")
    public ResponseEntity<RewardPointsResponse> getRewardPoints(@PathVariable Long accountId) {
        RewardPointsResponse response = rewardService.getRewardPoints(accountId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get reward transaction history for an account.
     * @param accountId The account ID
     * @return List of RewardTransactionResponse
     */
    @GetMapping("/history/{accountId}")
    public ResponseEntity<List<RewardTransactionResponse>> getRewardHistory(@PathVariable Long accountId) {
        List<RewardTransactionResponse> history = rewardService.getRewardHistory(accountId);
        return ResponseEntity.ok(history);
    }

    /**
     * Initialize reward points for a new account.
     * @param accountId The account ID
     * @return Response message
     */
    @PostMapping("/initialize/{accountId}")
    public ResponseEntity<String> initializeRewardPoints(@PathVariable Long accountId) {
        rewardService.initializeRewardPoints(accountId);
        return ResponseEntity.ok("Reward points initialized for account " + accountId);
    }
}
