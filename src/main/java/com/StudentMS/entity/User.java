package com.StudentMS.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50) // Limit username length
    private String username;

    @Column(length = 100) //Limit name length
    private String name;

    @Column(nullable = false, length = 255) // Store hashed password
    private String password;

    @Column(unique = true, length = 100) // Limit email length
    private String email;

    @Column(length = 20) //Limit phone length
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(nullable = false, length = 50) // Limit role length
    private String role;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public User(Long id, String username, String name, String password, String email, String phone, Department department, Course course, String role) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.department = department;
        this.course = course;
        this.role = role;
    }

    // Constructor to hash password.
    public User(String username, String name, String password, String email, String phone, Department department, Course course, String role) {
        this.username = username;
        this.name = name;
        this.password = new BCryptPasswordEncoder().encode(password); // Hash password
        this.email = email;
        this.phone = phone;
        this.department = department;
        this.course = course;
        this.role = role;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
//
//    public void setPassword(String password) {
//        this.password = new BCryptPasswordEncoder().encode(password);
//    }
}