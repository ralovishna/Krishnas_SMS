package com.StudentMS.controller;

import com.StudentMS.service.CourseService;
import com.StudentMS.service.DepartmentService;
import com.StudentMS.service.StudentService;
import com.StudentMS.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final CourseService courseService;
    private final DepartmentService departmentService;

    @Autowired
    public HomeController(StudentService studentService, TeacherService teacherService, CourseService courseService, DepartmentService departmentService) {
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.courseService = courseService;
        this.departmentService = departmentService;
    }

    @GetMapping("/dashboard") // Corrected mapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STUDENT', 'ROLE_TEACHER')")
    public String showDashboard(Model model) {
        model.addAttribute("totalStudents", studentService.getTotalStudents());
        model.addAttribute("totalTeachers", teacherService.getTotalTeachers());
        model.addAttribute("totalCourses", courseService.getTotalCourses());
        model.addAttribute("totalDepartments", departmentService.getTotalDepartments());
        return "Dashboard";
    }
}