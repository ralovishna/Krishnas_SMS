package com.StudentMS.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "courses")
@Data
@Builder // Add builder for easier object creation
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false) // Prevent manual ID updates
    private Long id;

    @Column(name = "course_code", unique = true, nullable = false, length = 20) // Add length constraint
    private String courseCode;

    @Column(name = "course_name", nullable = false, length = 30) // Add length constraint
    private String courseName;

    @Column(name = "description", length = 70) // Add length constraint
    private String description;

    @ManyToOne(fetch = FetchType.LAZY) // Lazy loading for better performance
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Column(name = "credits", nullable = false)
    private int credits;

    @Column(name = "fees", nullable = false)
    private int fees;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY) // Lazy loading for better performance
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}