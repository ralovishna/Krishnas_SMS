package com.StudentMS.controller;

import com.StudentMS.entity.User;
import com.StudentMS.service.StudentService;
import com.StudentMS.service.TeacherService;
import com.StudentMS.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {

    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);
    private final UserService userService;
    private final StudentService studentService;
    private final TeacherService teacherService;

    @Autowired
    public ProfileController(UserService userService, StudentService studentService, TeacherService teacherService) {
        this.userService = userService;
        this.studentService = studentService;
        this.teacherService = teacherService;
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STUDENT', 'ROLE_TEACHER')")
    public String getProfile(Model model) {
        User user = userService.getCurrentUser();

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_TEACHER"))) {
                teacherService.findById(user.getId()).ifPresentOrElse(
                        teacher -> model.addAttribute("teacher", teacher),
                        () -> logger.error("Teacher not found for user: {}", user.getId())
                );
            } else if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_STUDENT"))) {
                studentService.findById(user.getId()).ifPresentOrElse(
                        student -> model.addAttribute("student", student),
                        () -> logger.error("Student not found for user: {}", user.getId())
                );
            }
        }
        return "Profile";
    }
}