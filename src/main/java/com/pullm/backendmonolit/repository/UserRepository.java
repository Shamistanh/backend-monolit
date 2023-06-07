package com.pullm.backendmonolit.repository;

import com.pullm.backendmonolit.entities.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findUserByPhoneNumber(String phoneNumber);

  boolean existsUserByPhoneNumber(String phoneNumber);
}
