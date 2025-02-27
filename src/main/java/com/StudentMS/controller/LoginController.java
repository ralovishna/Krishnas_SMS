package com.StudentMS.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/login")
public class LoginController {
//
//    private final UserService userService;
//
//    public LoginController(UserService userService) {
//        this.userService = userService;
//    }

    @GetMapping
    public String showLoginForm(@RequestParam(value = "error", required = false) String error,
                                @RequestParam(value = "logout", required = false) String logout,
                                Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid username or password");
        }
        if (logout != null) {
            model.addAttribute("logoutMessage", "You have been logged out successfully");
        }
        return "dummy";  // Return login page with error message
    }

//    @PostMapping
//    public String login(
//            @RequestParam String username,
//            @RequestParam String password,
//            Model model
//    ) {
//        System.out.println("\n\n\n\n\n\n\n\n\nUsername: " + username);
//        System.out.println("Password: " + password);
//        if (userService.checkLogin(username, password))
//            return "redirect:/dashboard";  // Redirect to teacher dashboard
//        return "redirect:/register";
//    }
}