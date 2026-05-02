package com.example.studentcourse.repository;

import com.example.studentcourse.dto.StudentCourseDTO;
import com.example.studentcourse.entity.Course;
import com.example.studentcourse.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    private Course savedCourse;
    private Student savedStudent;

    @BeforeEach
    void setUp() {
        savedCourse = courseRepository.save(new Course("Test Course", "A test course", 3, "Test Instructor"));
        savedStudent = new Student("John", "Doe", "john.doe@test.com", 22);
        savedStudent.getCourses().add(savedCourse);
        savedStudent = studentRepository.save(savedStudent);
    }

    @Test
    void findByEmail_shouldReturnStudent_whenEmailExists() {
        Optional<Student> result = studentRepository.findByEmail("john.doe@test.com");
        assertThat(result).isPresent();
        assertThat(result.get().getFirstName()).isEqualTo("John");
    }

    @Test
    void findByEmail_shouldReturnEmpty_whenEmailDoesNotExist() {
        Optional<Student> result = studentRepository.findByEmail("nonexistent@test.com");
        assertThat(result).isEmpty();
    }

    @Test
    void findStudentsByCourseId_shouldReturnStudents_whenEnrolled() {
        List<Student> students = studentRepository.findStudentsByCourseId(savedCourse.getId());
        assertThat(students).hasSize(1);
        assertThat(students.get(0).getEmail()).isEqualTo("john.doe@test.com");
    }

    @Test
    void findStudentsByCourseId_shouldReturnEmpty_whenNoneEnrolled() {
        Course anotherCourse = courseRepository.save(new Course("Empty Course", "No students", 2, "No One"));
        List<Student> students = studentRepository.findStudentsByCourseId(anotherCourse.getId());
        assertThat(students).isEmpty();
    }

    @Test
    void findAllEnrollments_shouldReturnDTOs_whenEnrollmentsExist() {
        List<StudentCourseDTO> enrollments = studentRepository.findAllEnrollments();
        assertThat(enrollments).isNotEmpty();
        assertThat(enrollments.get(0).getCourseTitle()).isEqualTo("Test Course");
    }

    @Test
    void findByIdWithCourses_shouldReturnStudentWithCourses() {
        Optional<Student> result = studentRepository.findByIdWithCourses(savedStudent.getId());
        assertThat(result).isPresent();
        assertThat(result.get().getCourses()).hasSize(1);
    }

    @Test
    void save_shouldPersistStudent() {
        Student newStudent = new Student("Jane", "Smith", "jane.smith@test.com", 20);
        Student persisted = studentRepository.save(newStudent);
        assertThat(persisted.getId()).isNotNull();
        assertThat(studentRepository.findById(persisted.getId())).isPresent();
    }
}
