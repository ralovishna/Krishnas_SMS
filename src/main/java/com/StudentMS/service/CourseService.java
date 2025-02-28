package com.StudentMS.service;

import com.StudentMS.entity.Course;

import java.util.List;

public interface CourseService {

    Object getTotalCourses();

    List<Course> getAllCourses();

    Course getCourseById(Long id);

    void saveCourse(Course course);

    void deleteCourse(Long id);
}