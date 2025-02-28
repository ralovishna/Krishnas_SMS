package com.StudentMS.controller;

import com.StudentMS.entity.Department;
import com.StudentMS.service.DepartmentService;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STUDENT', 'ROLE_TEACHER')")
    public String listDepartments(@NotNull Model model) {
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "dept/IndexDepartment";
    }

    @GetMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String showAddDepartmentForm(@NotNull Model model) {
        model.addAttribute("department", new Department());
        return "dept/CreateDepartment";
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public String saveDepartment(@Valid @ModelAttribute("department") Department department, @NotNull BindingResult result) {
        if (result.hasErrors()) {
            return "dept/CreateDepartment";
        }
        departmentService.saveDepartment(department);
        return "redirect:/departments";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String showEditDepartmentForm(@PathVariable Long id, Model model) {
        Optional<Department> departmentOptional = departmentService.getDepartmentByIdOptional(id);
        if (departmentOptional.isEmpty()) {
            return "redirect:/departments";
        }
        model.addAttribute("department", departmentOptional.get());
        return "dept/EditDepartment";
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateDepartment(@PathVariable Long id, @Valid @ModelAttribute("department") Department department, @NotNull BindingResult result) {
        if (result.hasErrors()) {
            return "dept/EditDepartment";
        }

        Optional<Department> existingDepartmentOptional = departmentService.getDepartmentByIdOptional(id);
        if (existingDepartmentOptional.isEmpty()) {
            return "redirect:/departments";
        }

        Department existingDepartment = existingDepartmentOptional.get();
        existingDepartment.setName(department.getName());
        departmentService.saveDepartment(existingDepartment);
        return "redirect:/departments";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return "redirect:/departments";
    }
}