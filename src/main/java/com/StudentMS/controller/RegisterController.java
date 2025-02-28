package com.StudentMS.controller;

import com.StudentMS.entity.User;
import com.StudentMS.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/register")
public class RegisterController {

    private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);

    private final UserService userService;

    @Autowired
    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showRegistrationForm() {
        return "Register";
    }

    @PostMapping
    public ResponseEntity<String> registerUser(@RequestParam("username") String username,
                                               @RequestParam("password") String password,
                                               @RequestParam("role") String role) {

        logger.info("Attempting to register user: {}", username);

        if (userService.isUsernameExist(username, null)) {
            logger.warn("Username already exists: {}", username);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists.");
        }

        try {
            User user = userService.registerUser(username, password, role);

            if (user == null) {
                logger.error("Registration failed for user: {}", username);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed. Please try again.");
            }

            logger.info("User registered successfully: {}", username);

            String redirectUrl = getRedirectUrl(user.getRole());
            return ResponseEntity.ok(redirectUrl);

        } catch (Exception e) {
            logger.error("Error during registration: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during registration.");
        }
    }

    private String getRedirectUrl(String role) {
        return switch (role) {
            case "NEW_STUDENT" -> "/students/new";
            case "NEW_TEACHER" -> "/teachers/new";
            default -> "/register";
        };
    }
}