package com.library.book_stock.repository;

import com.library.book_stock.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByRecoveryCode(String recoveryCode);
}
