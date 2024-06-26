package com.example.chatapptrpo.repositories;

import com.example.chatapptrpo.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String roleName);
}

