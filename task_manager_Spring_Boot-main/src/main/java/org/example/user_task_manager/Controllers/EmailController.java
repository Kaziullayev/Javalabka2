package org.example.user_task_manager.Controllers;

import org.example.user_task_manager.Services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.mail.MessagingException;
@Controller
public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/send-email")
    public String showEmailForm() {
        return "send_email";
    }

    @PostMapping("/send-email")
    public String sendEmail(
            @RequestParam("to") String to,
            @RequestParam("subject") String subject,
            @RequestParam("body") String body,
            Model model
    ) {
        try {
            emailService.sendEmail(to, subject, body);
            model.addAttribute("message", "Email sent successfully!");
        } catch (MessagingException e) {
            model.addAttribute("error", "Failed to send email: " + e.getMessage());
        }
        return "send_email";
    }
}
