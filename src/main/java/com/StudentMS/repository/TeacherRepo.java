package com.StudentMS.repository;

import com.StudentMS.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherRepo extends JpaRepository<Teacher, Long> {
    @Query("SELECT t FROM Teacher t LEFT JOIN FETCH t.department")
    List<Teacher> findAllWithDepartments();

    boolean existsByUsernameAndIdNot(String username, Long studentId);

//    Optional<Object> findByUserId(Long id);
}