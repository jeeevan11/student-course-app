package com.example.studentcourse.service;

import com.example.studentcourse.dto.StudentCourseDTO;
import com.example.studentcourse.entity.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {

    List<Student> findAll();

    Optional<Student> findById(Long id);

    Optional<Student> findByIdWithCourses(Long id);

    Student save(Student student);

    Student update(Student student);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    List<Student> findByCourseId(Long courseId);

    List<StudentCourseDTO> findAllEnrollments();
}
