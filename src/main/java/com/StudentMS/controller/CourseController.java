package com.StudentMS.controller;

import com.StudentMS.entity.Course;
import com.StudentMS.service.CourseService;
import com.StudentMS.service.DepartmentService;
import com.StudentMS.service.TeacherService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;
    private final DepartmentService departmentService;
    private final TeacherService teacherService;

    public CourseController(CourseService courseService, DepartmentService departmentService, TeacherService teacherService) {
        this.courseService = courseService;
        this.departmentService = departmentService;
        this.teacherService = teacherService;
    }

    // ✅ List all courses
    @GetMapping
    public String listCourses(Model model) {
        model.addAttribute("courses", courseService.getAllCourses());
        return "cour/IndexCourse";
    }

    // ✅ Show form to create a new course
    @GetMapping("/new")
    public String showAddCourseForm(Model model) {
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("teachers", teacherService.getAllTeachers());
        model.addAttribute("course", new Course());
        return "cour/CreateCourse";
    }

    // ✅ Save a new course
    @PostMapping("/save")
    public String saveCourse(@Valid @ModelAttribute("course") Course course, BindingResult result) {
        if (result.hasErrors()) {
            return "course/editCourse";
        }
        // Set default end date if it's null
        if (course.getEndDate() == null) {
            course.setStartDate(LocalDate.now().plusMonths(3));
            course.setEndDate(LocalDate.now().plusMonths(9)); // Default to 6 months from today
        }
        System.out.println("Saving Course - End Date: " + course.getEndDate());

        courseService.saveCourse(course);
        return "redirect:/courses";
    }

    // ✅ Show form to edit an existing course
    @GetMapping("/edit/{id}")
    public String showEditCourseForm(@PathVariable Long id, Model model) {
        Course course = courseService.getCourseById(id);
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("teachers", teacherService.getAllTeachers());

        if (course == null) {
            return "redirect:/courses";
        }
        model.addAttribute("course", course);
        return "cour/EditCourse";  // Fixed incorrect view path
    }

    // ✅ Delete a course
    @GetMapping("/delete/{id}")
    public String deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return "redirect:/courses";
    }
}