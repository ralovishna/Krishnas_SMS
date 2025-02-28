package com.StudentMS.service;

import com.StudentMS.entity.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {

    Object getTotalDepartments();

    List<Department> getAllDepartments();

    Department getDepartmentById(Long id);

    void saveDepartment(Department department);

    void deleteDepartment(Long id);

    void updateDepartment(Department department);

    Optional<Department> getDepartmentByIdOptional(Long id);
}