package com.example.studentcourse.service;

import com.example.studentcourse.entity.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {

    List<Course> findAll();

    Optional<Course> findById(Long id);

    Course save(Course course);

    Course update(Course course);

    boolean existsByTitle(String title);

    boolean existsByTitleAndIdNot(String title, Long id);

    List<Course> findByStudentId(Long studentId);
}
