package org.example.user_task_manager.Controllers;

import org.example.user_task_manager.Entities.User;
import org.example.user_task_manager.Repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/profile")
    public String getProfilePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Получаем email текущего пользователя


        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/profile/upload")
    public String uploadProfilePicture(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                // Здесь можно сохранить файл в базу данных или файловую систему
                byte[] fileContent = file.getBytes();
                // Логика сохранения файла (реализуйте по необходимости)
                System.out.println("Uploaded file size: " + fileContent.length);
            } catch (IOException e) {
                e.printStackTrace();
                return "redirect:/profile?error";
            }
        }
        return "redirect:/profile?success";
    }

}

