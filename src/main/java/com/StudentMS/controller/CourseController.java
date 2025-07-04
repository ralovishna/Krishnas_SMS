package com.StudentMS.controller;

import com.StudentMS.entity.Course;
import com.StudentMS.service.CourseService;
import com.StudentMS.service.DepartmentService;
import com.StudentMS.service.TeacherService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/courses")
public class CourseController {

    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);
    private final CourseService courseService;
    private final DepartmentService departmentService;
    private final TeacherService teacherService;

    @Autowired
    public CourseController(CourseService courseService, DepartmentService departmentService, TeacherService teacherService) {
        this.courseService = courseService;
        this.departmentService = departmentService;
        this.teacherService = teacherService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STUDENT', 'ROLE_TEACHER')")
    public String listCourses(Model model) {
        logger.info("Listing all courses");
        model.addAttribute("courses", courseService.getAllCourses());
        return "cour/IndexCourse";
    }

    @GetMapping("/new")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String showAddCourseForm(Model model) {
        logger.info("Showing add course form");
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("teachers", teacherService.getAllTeachers());
        model.addAttribute("course", new Course());
        return "cour/CreateCourse";
    }

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String saveCourse(@Valid @ModelAttribute("course") Course course, BindingResult result) {
        logger.info("Saving course: {}", course);
        if (result.hasErrors()) {
            logger.warn("Validation errors: {}", result.getAllErrors());
            return "cour/EditCourse";
        }

        if (course.getEndDate() == null) {
            course.setStartDate(LocalDate.now().plusMonths(3));
            course.setEndDate(LocalDate.now().plusMonths(9));
        }

        courseService.saveCourse(course);
        logger.info("Course saved successfully: {}", course);
        return "redirect:/courses";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String showEditCourseForm(@PathVariable Long id, Model model) {
        logger.info("Showing edit course form for ID: {}", id);
        Course course = courseService.getCourseById(id);
        if (course == null) {
            logger.warn("Course not found with ID: {}", id);
            return "redirect:/courses";
        }

        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("teachers", teacherService.getAllTeachers());
        model.addAttribute("course", course);
        return "cour/EditCourse";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String deleteCourse(@PathVariable Long id) {
        logger.info("Deleting course with ID: {}", id);
        courseService.deleteCourse(id);
        return "redirect:/courses";
    }
}