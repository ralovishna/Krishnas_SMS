package com.StudentMS.service;

import com.StudentMS.entity.Teacher;
import com.StudentMS.entity.User;

import java.util.List;
import java.util.Optional;

public interface TeacherService {
//    List<Teacher> findAllWithDepartments();

    void saveTeacher(Teacher teacher);

//    Teacher getTeacherById(Long id);

//    Teacher updateTeacher(Teacher teacher);

    Teacher getTeacherById(Long id);

    void deleteTeacherById(Long id);

//    boolean checkLogin(Long id, String password);

    Teacher getAuthenticatedTeacherById(Long id);

    Object getTotalTeachers();

    List<Teacher> getAllTeachers();

    Optional<User> findById(Long id);

//    Teacher getAuthenticatedTeacher();

//    Optional<Object> findByUserId(Long id);
}