package com.StudentMS.serviceImpl;

import com.StudentMS.entity.Department;
import com.StudentMS.repository.DepartmentRepo;
import com.StudentMS.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private final DepartmentRepo departmentRepo;

    public DepartmentServiceImpl(DepartmentRepo departmentRepo) {
        this.departmentRepo = departmentRepo;
    }

    @Override
    public Object getTotalDepartments() {
        return departmentRepo.count();
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepo.findAll();
    }

    @Override
    public Department getDepartmentById(Long id) {
        return departmentRepo.findById(id).get();
    }

    @Override
    public void saveDepartment(Department department) {
        departmentRepo.save(department);
    }

    @Override
    public void deleteDepartment(Long id) {
        departmentRepo.deleteById(id);
    }

    @Override
    public void updateDepartment(Department department) {
        departmentRepo.save(department);
    }

    @Override
    public Optional<Department> getDepartmentByIdOptional(Long id) {
        return departmentRepo.findById(id);
    }
}