package com.example.studentcourse.repository;

import com.example.studentcourse.dto.StudentCourseDTO;
import com.example.studentcourse.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByEmail(String email);

    List<Student> findByLastName(String lastName);

    @Query("SELECT s FROM Student s JOIN s.courses c WHERE c.id = :courseId")
    List<Student> findStudentsByCourseId(@Param("courseId") Long courseId);

    @Query("SELECT new com.example.studentcourse.dto.StudentCourseDTO(s.firstName, s.lastName, c.title, c.instructor) " +
           "FROM Student s JOIN s.courses c ORDER BY s.lastName, s.firstName")
    List<StudentCourseDTO> findAllEnrollments();

    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.courses WHERE s.id = :id")
    Optional<Student> findByIdWithCourses(@Param("id") Long id);
}
