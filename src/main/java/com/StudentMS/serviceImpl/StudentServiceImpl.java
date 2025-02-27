package com.StudentMS.serviceImpl;

import com.StudentMS.entity.Student;
import com.StudentMS.entity.User;
import com.StudentMS.repository.StudentRepo;
import com.StudentMS.repository.UserRepo;
import com.StudentMS.service.StudentService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepo studentRepo;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public StudentServiceImpl(StudentRepo studentRepo, UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.studentRepo = studentRepo;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public List<Student> getAllStudents() {
        return studentRepo.findAll();
    }

    @Override
    public void saveStudent(Student student) {
        student.setRole("ROLE_STUDENT");
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        studentRepo.save(student);
    }

    @Override
    public Student getStudentById(Long id) {
        return studentRepo.findById(id).get();
    }

    @Override
    public Student updateStudent(Student student) {
        return studentRepo.save(student);
    }

    @Override
    public void deleteStudentById(Long id) {
        studentRepo.deleteById(id);
    }

    @Override
    public boolean checkLogin(Long id, String password) {
        return studentRepo.findById(id).isPresent() && studentRepo.findById(id).get().getPassword().equals(password);
    }

    @Override
    public Object getTotalStudents() {
        return studentRepo.count();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepo.findById(id);
    }

    public boolean isUsernameAlreadyExists(String username, Long id) {
        return studentRepo.existsByUsernameAndIdNot(username, id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

//    @Override
//    public Optional<Object> findByUserId(Long id) {
//        return studentRepo.findByUserId(id);
//    }

}