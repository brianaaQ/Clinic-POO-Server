package org.example.dao;

import org.example.entities.User;
import java.util.List;
import java.util.Optional;

public interface UserDAO {
    Long save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndName(String email, String name);
    List<User> findAll();
    void update(User user);
    void delete(Long id);
    boolean existsByEmail(String email);
} 