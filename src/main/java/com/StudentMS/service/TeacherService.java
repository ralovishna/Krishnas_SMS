package com.StudentMS.service;

import com.StudentMS.entity.Teacher;
import com.StudentMS.entity.User;

import java.util.List;
import java.util.Optional;

public interface TeacherService {
    List<Teacher> findAllWithDepartments();

    void saveTeacher(Teacher teacher);

    Teacher getTeacherById(Long id);

    Teacher updateTeacher(Teacher teacher);

    void deleteTeacherById(Long id);

    boolean checkLogin(Long id, String password);

    Object getTotalTeachers();

    List<Teacher> getAllTeachers();

    Optional<User> findById(Long id);

//    Optional<Object> findByUserId(Long id);
}