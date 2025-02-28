package com.StudentMS.repository;

import com.StudentMS.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepo extends JpaRepository<Student, Long> {
//    Optional<Student> findByUsername(String username);

//    boolean existsByUsernameAndIdNot(String username, Long studentId);
//    Optional<Object> findByUserId(Long id);
}