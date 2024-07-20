package com.pullm.backendmonolit.repository;

import com.pullm.backendmonolit.entities.Goal;
import com.pullm.backendmonolit.entities.User;
import com.pullm.backendmonolit.enums.GoalStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface GoalRepository extends JpaRepository<Goal, Long> {

    Optional<Goal> findByIdAndUser(long id, User userId);

    List<Goal> findAllByUser(User userId);

    @Transactional
    void deleteByIdAndUser(Long id, User userId);

    List<Goal> findAllByUserAndStatus(User userId, GoalStatus status);

}
