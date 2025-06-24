package com.adi.user_service.repo;

import com.adi.user_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    boolean existsByUsername(String username);

    User findByUsername(String name);
}
