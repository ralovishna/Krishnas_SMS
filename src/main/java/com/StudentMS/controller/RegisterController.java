package com.StudentMS.controller;

import com.StudentMS.entity.User;
import com.StudentMS.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/register")
public class RegisterController {


    private final UserService userService;

    @Autowired
    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showRegistrationForm() {
        return "Register"; // Render the registration form (Register.html)
    }

    @PostMapping
    public String registerUser(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               @RequestParam("role") String role,
                               HttpServletResponse response,
                               RedirectAttributes redirectAttributes) {

//        logger.debug("Attempting to register user: {}", username);

        // Check if the username already exists
        if (userService.isUsernameExist(username, null)) {
//            logger.warn("Username already exists: {}", username);
            redirectAttributes.addFlashAttribute("error", "Username already exists.");
            return "redirect:/register";
        }

        // Prefix the role with "NEW_" (or any other logic you need)
        String prefixedRole = "NEW_" + role.toUpperCase();
        System.out.println("\n\n\n\n\n\n\n\n\n\n\nPrefixed role: {}" + prefixedRole);

        // Register the user
        User user = userService.registerUser(username, password, prefixedRole);

        if (user == null) {
            System.out.println("Registration failed for user: {}" + username);
            redirectAttributes.addFlashAttribute("error", "Registration failed. Please try again.");
            return "redirect:/register";
        }

        System.out.println("User registered successfully: {}" + username);

        // Redirect based on role
        switch (prefixedRole) {
            case "NEW_STUDENT":
                System.out.println("Redirecting to student dashboard for user: {}" + username);
                return "redirect:/students/new";
            case "NEW_TEACHER":
                System.out.println("Redirecting to teacher dashboard for user: {}" + username);
                return "redirect:/teachers/new";
            default:
                System.out.println("Invalid role selected: {}" + prefixedRole);
                redirectAttributes.addFlashAttribute("error", "Invalid role selected.");
                return "redirect:/register";
        }
    }
}