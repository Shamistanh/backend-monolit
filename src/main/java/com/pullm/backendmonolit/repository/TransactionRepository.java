package com.pullm.backendmonolit.repository;

import com.pullm.backendmonolit.entities.Transaction;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByUserIdAndDateBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate);

    List<Transaction> findAllByUserId(Long userId);
}
