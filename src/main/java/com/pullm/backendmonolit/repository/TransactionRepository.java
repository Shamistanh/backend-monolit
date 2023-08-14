package com.pullm.backendmonolit.repository;

import com.pullm.backendmonolit.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByUserIdAndDateBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate);
}
