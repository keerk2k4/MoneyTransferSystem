package com.fidelity.mts.repo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.fidelity.mts.entity.TransactionLog;

@Repository
public interface TransactionLogRepo extends JpaRepository<TransactionLog, UUID> {

    List<TransactionLog> findByFromAccountIdOrToAccountId(Long fromId, Long toId);
    List<TransactionLog> findByFromAccountId(Long fromId);
    List<TransactionLog> findByToAccountId(Long toId);

    Optional<TransactionLog> findByIdempotencyKey(String idempotencyKey);
}
