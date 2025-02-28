package com.StudentMS.controller;

import com.StudentMS.entity.Student;
import com.StudentMS.entity.User;
import com.StudentMS.service.DepartmentService;
import com.StudentMS.service.StudentService;
import com.StudentMS.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/students")
public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);
    private final UserService userService;
    private final StudentService studentService;
    private final DepartmentService departmentService;

    @Autowired
    public StudentController(UserService userService, StudentService studentService, DepartmentService departmentService) {
        this.userService = userService;
        this.studentService = studentService;
        this.departmentService = departmentService;
    }

    private void addDepartmentListToModel(Model model) {
        model.addAttribute("departments", departmentService.getAllDepartments());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'STUDENT')")
    public String listStudents(Model model) {
        List<Student> students = studentService.getAllStudents();
        model.addAttribute("students", students);
        return "stud/IndexStudent";
    }

    @GetMapping("/new")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public String showAddStudentForm(Model model) {
        model.addAttribute("student", new Student());
        addDepartmentListToModel(model);
        return "stud/CreateStudent";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_TEACHER', 'ROLE_STUDENT')")
    public String editStudentForm(@PathVariable Long id, Model model, Principal principal) {
        if (principal != null) {
            String loggedInUsername = principal.getName();

            Optional<User> studentOptional = studentService.findById(id);

            if (studentOptional.isPresent()) {
                Object obj = studentOptional.get();
                if (obj instanceof Student student && (loggedInUsername.equals(student.getUsername()) || hasAuth(principal))) {
                    model.addAttribute("student", student);
                    return "stud/EditStudent";
                }
            }
        }

        return "ErrorPage";
    }

    private boolean hasAuth(Principal principal) {
        return SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN") || grantedAuthority.getAuthority().equals("ROLE_TEACHER"));
    }

    @PostMapping("/save")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'STUDENT')")
    public String saveStudent(@Valid @ModelAttribute("student") Student student,
                              BindingResult bindingResult,
                              Model model,
                              @AuthenticationPrincipal UserDetails userDetails) {
        logger.info("saveStudent called for student ID: {}, by user: {}", student.getId(), userDetails.getUsername());

        boolean isUsernameTaken = userService.isUsernameExist(student.getUsername(), student.getId());
        if (isUsernameTaken) {
            bindingResult.rejectValue("username", "username.duplicate", "Username already exists.");
            logger.warn("Duplicate username found: {}", student.getUsername());
        }

        if (bindingResult.hasErrors()) {
            addDepartmentListToModel(model);
            logger.warn("Validation errors: {}", bindingResult.getAllErrors());
            return "stud/EditStudent";
        }

        studentService.saveStudent(student);
        logger.info("Student saved successfully: {}", student);
        return "redirect:/students";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteStudent(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        logger.info("Student with id: {}, was deleted by user: {}", id, userDetails.getUsername());
        studentService.deleteStudentById(id);
        return "redirect:/students";
    }
}