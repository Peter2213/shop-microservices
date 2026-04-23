package com.example.my_voucher_app.repo;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.my_voucher_app.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
