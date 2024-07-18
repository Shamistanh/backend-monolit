package com.pullm.backendmonolit.repository;

import com.pullm.backendmonolit.entities.UserFinance;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFinancialConditionsRepository extends JpaRepository<UserFinance, Long> {

    Optional<UserFinance> findByUserId(long userId);

}
