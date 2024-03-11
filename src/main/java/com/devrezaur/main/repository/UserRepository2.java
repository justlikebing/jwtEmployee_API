package com.devrezaur.main.repository;

import com.devrezaur.main.model.User2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository2 extends JpaRepository<User2, Long> {
    // You can define additional query methods here if needed
}
