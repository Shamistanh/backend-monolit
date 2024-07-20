package com.pullm.backendmonolit.repository;

import com.pullm.backendmonolit.entities.User;
import com.pullm.backendmonolit.entities.UserIncome;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserIncomeRepository extends JpaRepository<UserIncome, Long> {

    List<UserIncome> findAllByUserIdAndDateBetween(long userId, LocalDateTime startDate, LocalDateTime endDate);

    @Transactional
    void deleteByIdAndUser(Long id, User user);

}
