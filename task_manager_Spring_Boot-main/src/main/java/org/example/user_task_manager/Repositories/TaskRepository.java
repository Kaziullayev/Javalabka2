package org.example.user_task_manager.Repositories;

import org.example.user_task_manager.Entities.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByUserUsername(String username, Pageable pageable);

    Page<Task> findByUserUsernameAndTitleContainingIgnoreCase(String username, String title, Pageable pageable);

    Page<Task> findByUserUsernameAndStatus(String username, String status, Pageable pageable);
}


