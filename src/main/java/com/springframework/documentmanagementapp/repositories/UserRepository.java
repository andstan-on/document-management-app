package com.springframework.documentmanagementapp.repositories;

import com.springframework.documentmanagementapp.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>, UserSearchDAO {

    Page<User> findAll(Pageable pageable);

    List<User> findAll();

    Page<User> filterUsers(String searchedCriteria, Pageable pageable);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

}
