package com.StudentMS.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(unique = true, nullable = false)
    private String username;

    @Column()
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String email;

    private String phone;

    @ManyToOne(fetch = FetchType.LAZY) //Lazy Fetch.
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY) //Lazy Fetch.
    @JoinColumn(name = "course_id")
    private Course course;

    //    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private String role;

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
}