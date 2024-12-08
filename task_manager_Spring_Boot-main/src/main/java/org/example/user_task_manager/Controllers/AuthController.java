package org.example.user_task_manager.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // login.html в папке templates
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register"; // register.html в папке templates
    }


}

