package org.example.user_task_manager.Controllers;

import org.example.user_task_manager.Entities.User;
import org.example.user_task_manager.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String email,
            @RequestParam String birth_date,
            @RequestParam String phone_number,
            @RequestParam String gender,
            Model model
    ) {
        if (!gender.equals("male") && !gender.equals("female")) {
            model.addAttribute("error", "Gender must be either 'male' or 'female'");
            return "register";
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setBirthDate(LocalDate.parse(birth_date));
        user.setPhoneNumber(phone_number);
        user.setGender(gender);

        userRepository.save(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/profile")
    public String showProfile(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/profile/upload")
    public String uploadProfilePhoto(@RequestParam("photo") MultipartFile photo, Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        if (!photo.isEmpty()) {
            try {
                String uploadDir = "src/main/resources/static/uploads/";
                File directory = new File(uploadDir);

                if (!directory.exists()) {
                    directory.mkdirs();
                }

                String filename = System.currentTimeMillis() + "_" + photo.getOriginalFilename();
                File uploadFile = new File(uploadDir + filename);

                photo.transferTo(uploadFile);

                user.setPhotoUrl("/uploads/" + filename);
                userRepository.save(user);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return "redirect:/profile";
    }
}



