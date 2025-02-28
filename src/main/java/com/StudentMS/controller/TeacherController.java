package com.StudentMS.controller;

import com.StudentMS.entity.Teacher;
import com.StudentMS.entity.User;
import com.StudentMS.service.DepartmentService;
import com.StudentMS.service.TeacherService;
import com.StudentMS.service.UserService;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/teachers")
public class TeacherController {

    private static final Logger logger = LoggerFactory.getLogger(TeacherController.class);
    private final TeacherService teacherService;
    private final DepartmentService departmentService;
    private final UserService userService;


    @Autowired
    public TeacherController(TeacherService teacherService, DepartmentService departmentService, UserService userService) {
        this.teacherService = teacherService;
        this.departmentService = departmentService;
        this.userService = userService;
    }

    private void addDepartmentListToModel(@NotNull Model model) {
        model.addAttribute("departments", departmentService.getAllDepartments());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'STUDENT')")
    public String listTeachers(Model model) {
        List<Teacher> teachers = teacherService.getAllTeachers();
        addDepartmentListToModel(model);
        model.addAttribute("teachers", teachers);
        return "teach/IndexTeacher";
    }

    @GetMapping("/new")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String showCreateTeacherForm(@NotNull Model model) {
        model.addAttribute("teacher", new Teacher());
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "teach/CreateTeacher";
    }

    @PostMapping("/save")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public String saveOrUpdateTeacher(@Valid @ModelAttribute("teacher") @NotNull Teacher teacher,
                                      BindingResult bindingResult,
                                      Model model) {
        // Check for duplicate username (if updating an existing teacher)
        boolean isUsernameTaken = userService.isUsernameExist(teacher.getUsername(), teacher.getId());
        if (isUsernameTaken) {
            bindingResult.rejectValue("username", "username.duplicate", "Username already exists.");
        }

        // Validate other fields
        if (bindingResult.hasErrors()) {
            addDepartmentListToModel(model);
            return "teach/EditTeacher"; // Return to the form with errors
        }

        // Save or update the teacher
        teacherService.saveTeacher(teacher);
        return "redirect:/teachers"; // Redirect to the teachers list page
    }

    private boolean hasRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_TEACHER')")
    public String editTeacherForm(@PathVariable Long id, Model model, Principal principal) {
        if (principal != null) {
            String loggedInUsername = principal.getName();

            Optional<User> teacherOptional = teacherService.findById(id);

            if (teacherOptional.isPresent()) {
                Object obj = teacherOptional.get();
                if (obj instanceof Teacher teacher && (loggedInUsername.equals(teacher.getUsername()) || hasAuth(principal))) {
                    model.addAttribute("teacher", teacher);
                    return "teach/EditTeacher";
                }
            }
        }

        return "ErrorPage";
    }

    private boolean hasAuth(Principal principal) {
        return SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacherById(id);
        return "redirect:/teachers";
    }
}