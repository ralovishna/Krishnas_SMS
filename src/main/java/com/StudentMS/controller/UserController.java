package com.StudentMS.controller;

import com.StudentMS.entity.User;
import com.StudentMS.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String getAllUsers(Model model) {
        logger.info("Getting all users");
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "us/IndexUser";
    }

    @GetMapping("/new")
    public String showAddUserForm(Model model) {
        logger.info("Showing add user form");
        model.addAttribute("user", new User());
        return "us/CreateUser";
    }

    @PostMapping("/save")
    public String saveUser(@Valid @ModelAttribute("user") User user, BindingResult result) {
        logger.info("Saving user: {}", user);

        if (result.hasErrors()) {
            logger.warn("Validation errors: {}", result.getAllErrors());
            return user.getId() != null ? "us/EditUser" : "us/CreateUser";
        }

        if (userService.isUsernameExist(user.getUsername(), user.getId())) {
            result.rejectValue("username", "error.user", "Username already exists");
            logger.warn("Duplicate username: {}", user.getUsername());
            return user.getId() != null ? "us/EditUser" : "us/CreateUser";
        }

        userService.saveUser(user);
        logger.info("User saved successfully: {}", user);

        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    public String showEditUserForm(@PathVariable Long id, Model model) {
        logger.info("Showing edit user form for ID: {}", id);
        User user = userService.getUserById(id);
        if (user == null) {
            logger.warn("User not found with ID: {}", id);
            return "redirect:/users";
        }
        model.addAttribute("user", user);
        return "us/EditUser";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        logger.info("Deleting user with ID: {}", id);
        userService.deleteUserById(id);
        return "redirect:/users";
    }
}