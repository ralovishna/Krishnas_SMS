package com.StudentMS.controller;

import com.StudentMS.entity.Student;
import com.StudentMS.repository.StudentRepo;
import com.StudentMS.service.DepartmentService;
import com.StudentMS.service.StudentService;
import com.StudentMS.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final UserService userService;
    private final StudentService studentService;
    private final DepartmentService departmentService;
    private final StudentRepo studentRepo;
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    public StudentController(StudentService studentService, DepartmentService departmentService, StudentRepo studentRepo, UserService userService) {
        this.studentService = studentService;
        this.departmentService = departmentService;
        this.studentRepo = studentRepo;
        this.userService = userService;
    }

    private void addDepartmentListToModel(Model model) {
        model.addAttribute("departments", departmentService.getAllDepartments());
    }

    @GetMapping
    public String listStudents(Model model) {
        List<Student> students = studentService.getAllStudents();
        model.addAttribute("students", students);
        return "stud/IndexStudent";
    }

    @GetMapping("/new")
    public String showAddStudentForm(Model model) {
        model.addAttribute("student", new Student());
        addDepartmentListToModel(model);
        return "stud/CreateStudent";
    }

    @GetMapping("/edit/{id}")
    public String editStudentForm(@PathVariable Long id, Model model) {
        logger.info("editStudentForm called for student ID: {}", id);
        Optional<Student> studentOptional = studentRepo.findById(id);

        if (studentOptional.isPresent()) {
            model.addAttribute("student", studentOptional.get());
            addDepartmentListToModel(model);
            logger.info("Student found for edit: {}", id);
            return "stud/EditStudent";
        } else {
            logger.warn("Student not found with ID: {}", id);
            return "redirect:/students";
        }
    }

    @PostMapping("/save")
    public String saveStudent(@Valid @ModelAttribute("student") Student student,
                              BindingResult bindingResult,
                              Model model) {
        logger.info("saveStudent called for student ID: {}", student.getId());

        // Check for duplicate username (if username is being updated)
        boolean isUsernameTaken = userService.isUsernameExist(student.getUsername(), student.getId());
        logger.info("Checking for duplicate username: {}", isUsernameTaken);
        if (isUsernameTaken) {
            bindingResult.rejectValue("username", "username.duplicate", "Username already exists.");
            logger.warn("Duplicate username found: {}", student.getUsername());
        }

        // Validate other fields
        if (bindingResult.hasErrors()) {
            addDepartmentListToModel(model);
            logger.warn("Validation errors: {}", bindingResult.getAllErrors());
            return "stud/EditStudent"; // Return to the form with errors
        }

        // Save the student if no errors
        studentService.saveStudent(student);
        logger.info("Student saved successfully: {}", student);
        return "redirect:/students"; // Redirect to the students list page
    }

    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentService.deleteStudentById(id);
        return "redirect:/students";
    }
}