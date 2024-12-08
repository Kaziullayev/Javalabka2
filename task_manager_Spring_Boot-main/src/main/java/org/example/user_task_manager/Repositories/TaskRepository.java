package org.example.user_task_manager.Repositories;

import org.example.user_task_manager.Entities.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t WHERE t.user.username = :username")
    Page<Task> findByUserUsername(@Param("username") String username, Pageable pageable);

    Page<Task> findByUserUsernameAndTitleContainingIgnoreCase(String username, String search, Pageable pageable);

    Page<Task> findByUserUsernameAndStatus(String username, String filter, Pageable pageable);
}


