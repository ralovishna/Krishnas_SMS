package com.StudentMS.repository;

import com.StudentMS.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsernameAndIdNot(String username, Long studentId); // Corrected signature


    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.password = :password, u.role = :role WHERE u.username = :username")
    int updatePasswordAndRole(@Param("username") String username,
                              @Param("password") String password,
                              @Param("role") String role);

    boolean existsByUsername(String username);

//    Optional<User> findByEmail(String name);

//    Optional<Student> findById(Long id);
//
//    Optional<Student> findByUserId(Long userId);
//
//    Optional<Teacher> findById(Long id);
//
//    Optional<Teacher> findByUserId(Long userId);
}