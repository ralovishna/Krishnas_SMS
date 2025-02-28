package com.StudentMS.serviceImpl;

import com.StudentMS.entity.Teacher;
import com.StudentMS.entity.User;
import com.StudentMS.repository.TeacherRepo;
import com.StudentMS.repository.UserRepo;
import com.StudentMS.service.TeacherService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherServiceImpl implements TeacherService {


    private static final Logger logger = LoggerFactory.getLogger(TeacherServiceImpl.class);
    private final TeacherRepo teacherRepo;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public TeacherServiceImpl(TeacherRepo teacherRepo, UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.teacherRepo = teacherRepo;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

//    @Override
//    public List<Teacher> findAllWithDepartments() {
//        return teacherRepo.findAllWithDepartments();
//    }

    @Override
    public void saveTeacher(@NotNull Teacher teacher) {
        teacher.setRole("ROLE_TEACHER");
        teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
        teacherRepo.save(teacher);
    }

    @Override
    public Teacher getTeacherById(Long id) {
        return teacherRepo.findById(id).orElse(null);
    }
//
//    @Override
//    public Teacher updateTeacher(Teacher teacher) {
//        return teacherRepo.save(teacher);
//    }

    @Override
    public void deleteTeacherById(Long id) {
        teacherRepo.deleteById(id);
    }


//    @Override
//    public boolean checkLogin(Long id, String password) {
//        return teacherRepo.findById(id).isPresent() && teacherRepo.findById(id).get().getPassword().equals(password);
//    }

    public Teacher getAuthenticatedTeacherById(Long id) {
        return teacherRepo.findById(id).orElse(null);
    }

    @Override
    public Object getTotalTeachers() {
        return teacherRepo.count();
    }

    @Override
    public List<Teacher> getAllTeachers() {
        return teacherRepo.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepo.findById(id);
    }

//    public Teacher getAuthenticatedTeacher() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || !authentication.isAuthenticated()) {
////            logger.info("No authenticated user");
//            return null;
//        }
//        String username = authentication.getName();
////        logger.info("Authenticated username: {}", username);
//        Optional<Teacher> teacherOptional = teacherRepo.findByUsername(username);
//
//        if (teacherOptional.isEmpty()) {
////            logger.info("Teacher not found by username: {}", username);
//            return null;
//        }
//
//        Teacher teacher = teacherOptional.get();
////        logger.info("Authenticated teacher ID: {}", teacher.getId());
//        return teacher;
//    }
//
//    public boolean isUsernameAlreadyExists(String username, Long id) {
//        return teacherRepo.existsByUsernameAndIdNot(username, id);
//    }

}