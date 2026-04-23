package com.example.my_voucher_app.repo;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.my_voucher_app.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByEmail(String email);
}
