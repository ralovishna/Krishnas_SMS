package com.StudentMS.controller;

import com.StudentMS.entity.Teacher;
import com.StudentMS.repository.TeacherRepo;
import com.StudentMS.service.DepartmentService;
import com.StudentMS.service.TeacherService;
import com.StudentMS.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherService teacherService;
    private final DepartmentService departmentService;
    private final PasswordEncoder passwordEncoder;
    private final TeacherRepo teacherRepo;
    private final UserService userService;

    @Autowired
    public TeacherController(TeacherService teacherService, DepartmentService departmentService, PasswordEncoder passwordEncoder, TeacherRepo teacherRepo, UserService userService) {
        this.teacherService = teacherService;
        this.departmentService = departmentService;
        this.passwordEncoder = passwordEncoder;
        this.teacherRepo = teacherRepo;
        this.userService = userService;
    }

    private void addDepartmentListToModel(Model model) {
        model.addAttribute("departments", departmentService.getAllDepartments());
    }

    @GetMapping
    public String listTeachers(Model model) {
        List<Teacher> teachers = teacherService.getAllTeachers();
        addDepartmentListToModel(model);
        model.addAttribute("teachers", teachers);
        return "teach/IndexTeacher";
    }

    @GetMapping("/new")
    public String showCreateTeacherForm(Model model) {
        model.addAttribute("teacher", new Teacher());
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "teach/CreateTeacher";
    }

    @PostMapping("/save")
    public String saveOrUpdateTeacher(@Valid @ModelAttribute("teacher") Teacher teacher,
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

    @GetMapping("/edit/{id}")
    public String editTeacherForm(@PathVariable Long id, Model model) {
        Optional<Teacher> teacherOptional = teacherRepo.findById(id);

        if (teacherOptional.isPresent()) {
            model.addAttribute("teacher", teacherOptional.get());
            addDepartmentListToModel(model);
            return "teach/EditTeacher";
        } else {
            return "redirect:/teachers";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacherById(id);
        return "redirect:/teachers";
    }
}