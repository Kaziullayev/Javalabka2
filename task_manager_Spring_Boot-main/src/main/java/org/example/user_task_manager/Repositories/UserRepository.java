package org.example.user_task_manager.Repositories;

import org.example.user_task_manager.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email); // Поиск пользователя по email

    boolean existsByEmail(String email); // Проверка на уникальность email

    Optional<User> findByUsername(String username); // Поиск пользователя по username (если нужно)
}