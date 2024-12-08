package org.example.user_task_manager.Controllers;

import org.example.user_task_manager.Entities.Task;
import org.example.user_task_manager.Entities.User;
import org.example.user_task_manager.Repositories.TaskRepository;
import org.example.user_task_manager.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Отображение задач с пагинацией, поиском и фильтрацией.
     */
    @GetMapping
    public String listTasks(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "filter", required = false) String filter,
            Model model,
            Authentication authentication
    ) {
        String username = authentication.getName();
        Pageable pageable = PageRequest.of(page, size);

        Page<Task> tasks;
        if (search != null && !search.isEmpty()) {
            tasks = taskRepository.findByUserUsernameAndTitleContainingIgnoreCase(username, search, pageable);
        } else if (filter != null && !filter.isEmpty()) {
            tasks = taskRepository.findByUserUsernameAndStatus(username, filter, pageable);
        } else {
            tasks = taskRepository.findByUserUsername(username, pageable);
        }

        model.addAttribute("tasks", tasks.getContent());
        model.addAttribute("currentPage", tasks.getNumber());
        model.addAttribute("totalPages", tasks.getTotalPages());
        model.addAttribute("search", search);
        model.addAttribute("filter", filter);
        return "tasks";
    }

    /**
     * Отображение формы для создания задачи.
     */
    @GetMapping("/new")
    public String showCreateTaskForm(Model model) {
        model.addAttribute("task", new Task());
        return "create_task";
    }

    /**
     * Создание новой задачи.
     */
    @PostMapping
    public String createTask(Task task, Authentication authentication) {
        String username = authentication.getName();

        // Обработка Optional<User>
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        task.setUser(user); // Установить пользователя для задачи
        taskRepository.save(task); // Сохранить задачу
        return "redirect:/tasks";
    }

    /**
     * Удаление задачи по идентификатору.
     */
    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskRepository.deleteById(id);
        return "redirect:/tasks";
    }
}


