package com.StudentMS.controller;

import com.StudentMS.entity.User;
import com.StudentMS.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')") // Restrict to ADMIN role
    public String getAllUsers(Model model) {
        logger.info("Getting all users");
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "us/IndexUser";
    }

    @GetMapping("/new")
    @PreAuthorize("hasRole('ADMIN')") // Restrict to ADMIN role
    public String showAddUserForm(Model model) {
        logger.info("Showing add user form");
        model.addAttribute("user", new User());
        return "us/CreateUser";
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')") // Restrict to ADMIN role
    public String saveUser(@Valid @ModelAttribute("user") User user, BindingResult result, RedirectAttributes redirectAttributes) {
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

        try {
            userService.saveUser(user);
            logger.info("User saved successfully: {}", user);
            redirectAttributes.addFlashAttribute("successMessage", "User saved successfully.");
        } catch (DataIntegrityViolationException e) {
            logger.error("Database integrity violation: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "An error occurred while saving the user. Please check the data.");
            return "redirect:/users/new";
        }

        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Restrict to ADMIN role
    public String showEditUserForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        logger.info("Showing edit user form for ID: {}", id);
        User user = userService.getUserById(id);
        if (user == null) {
            logger.warn("User not found with ID: {}", id);
            redirectAttributes.addFlashAttribute("errorMessage", "User not found.");
            return "redirect:/users";
        }
        model.addAttribute("user", user);
        return "us/EditUser";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Restrict to ADMIN role
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        logger.info("Deleting user with ID: {}", id);
        try {
            userService.deleteUserById(id);
            redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully.");
        } catch (Exception e) {
            logger.error("Error deleting user: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting user.");
        }
        return "redirect:/users";
    }
}