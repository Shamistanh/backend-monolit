package com.pullm.backendmonolit.repository;

import com.pullm.backendmonolit.entities.Goal;
import com.pullm.backendmonolit.entities.User;
import com.pullm.backendmonolit.enums.GoalPriority;
import com.pullm.backendmonolit.enums.GoalStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface GoalRepository extends JpaRepository<Goal, Long> {

    Optional<Goal> findByIdAndUser(long id, User userId);

    Boolean existsByUserAndGoalPriorityAndStatus(User user, GoalPriority goalPriority, GoalStatus status);

    List<Goal> findAllByUser(User userId);

    List<Goal> findAllByUserAndEndDateBeforeOrStatus(User userId, LocalDate endDate, GoalStatus status);

    List<Goal> findAllByUser(User userId, Pageable pageable);

    @Transactional
    void deleteByIdAndUser(Long id, User userId);

    List<Goal> findAllByUserAndStatus(User userId, GoalStatus status);

    List<Goal> findAllByUserAndStatus(User userId, GoalStatus status, Pageable pageable);

}
