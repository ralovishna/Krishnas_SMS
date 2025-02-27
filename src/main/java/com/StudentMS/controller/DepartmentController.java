package com.StudentMS.controller;

import com.StudentMS.entity.Department;
import com.StudentMS.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    // ✅ List all departments
    @GetMapping
    public String listDepartments(Model model) {
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "dept/IndexDepartment";
    }

    // ✅ Show form to create a new department
    @GetMapping("/new")
    public String showAddDepartmentForm(Model model) {
        // Create a new Department object and add it to the model
        model.addAttribute("department", new Department());
        // Return the view name to render the form
        return "dept/CreateDepartment";
    }

    // ✅ Save a new department (with validation)
    @PostMapping("/save")
    public String saveDepartment(@Valid @ModelAttribute("department") Department department, BindingResult result) {
        if (result.hasErrors()) {
            return "dept/CreateDepartment";  // Stay on the form if validation fails
        }
        departmentService.saveDepartment(department);  // Save the department
        return "redirect:/departments";  // Redirect after successful save
    }

    // ✅ Show form to edit a department
    @GetMapping("/edit/{id}")
    public String showEditDepartmentForm(@PathVariable Long id, Model model) {
        Department department = departmentService.getDepartmentById(id);
        if (department == null) {
            return "redirect:/departments";  // Redirect if not found
        }
        model.addAttribute("department", department);
        return "dept/EditDepartment";
    }

    // ✅ Update department (reuse same `/departments` endpoint)
    @PostMapping("/{id}")
    public String updateDepartment(@PathVariable Long id, @Valid @ModelAttribute("department") Department department, BindingResult result) {
        if (result.hasErrors()) {
            return "dept/EditDepartment";
        }

        Department existingDepartment = departmentService.getDepartmentById(id);
        if (existingDepartment == null) {
            return "redirect:/departments";  // Redirect if department not found
        }

        existingDepartment.setName(department.getName()); // Add more fields if needed
        departmentService.saveDepartment(existingDepartment);
        return "redirect:/departments";
    }

    // ✅ Delete a department
    @GetMapping("/delete/{id}")
    public String deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return "redirect:/departments";
    }
}