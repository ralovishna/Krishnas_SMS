package com.StudentMS.service;

import com.StudentMS.entity.Student;
import com.StudentMS.entity.User;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    List<Student> getAllStudents();

    void saveStudent(Student student);

    Student getStudentById(Long id);

//    Student updateStudent(Student student);

    void deleteStudentById(Long id);

//    boolean checkLogin(Long id, String password);

    Object getTotalStudents();

    Optional<User> findById(Long id);

//    boolean isUsernameAlreadyExists(String username, Long id);

//    Optional<User> findByUsername(String username);

//    Student getAuthenticatedStudentById(Long id);

//    Optional<Object> findByUserId(Long id);
}