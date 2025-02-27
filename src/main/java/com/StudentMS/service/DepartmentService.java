package com.StudentMS.service;

import com.StudentMS.entity.Department;

import java.util.List;

public interface DepartmentService {

    Object getTotalDepartments();

    List<Department> getAllDepartments();

    Department getDepartmentById(Long id);

    Object saveDepartment(Department department);

    void deleteDepartment(Long id);

    void updateDepartment(Department department);
}