package com.StudentMS.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "teachers") // Generates an all-args constructor
@PrimaryKeyJoinColumn(name = "user_id")
public class Teacher extends User {
    private String designation;
    @Column(name = "assigned_classes")
    private String assignedClasses;
    private String subjects;
}