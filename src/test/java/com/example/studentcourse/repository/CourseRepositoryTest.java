package com.example.studentcourse.repository;

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
class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    private Course savedCourse;

    @BeforeEach
    void setUp() {
        savedCourse = courseRepository.save(new Course("Java Fundamentals", "Intro to Java", 4, "Dr. Smith"));
    }

    @Test
    void findByTitle_shouldReturnCourse_whenTitleExists() {
        Optional<Course> result = courseRepository.findByTitle("Java Fundamentals");
        assertThat(result).isPresent();
        assertThat(result.get().getInstructor()).isEqualTo("Dr. Smith");
    }

    @Test
    void findByTitle_shouldReturnEmpty_whenTitleDoesNotExist() {
        Optional<Course> result = courseRepository.findByTitle("Nonexistent Course");
        assertThat(result).isEmpty();
    }

    @Test
    void findByInstructor_shouldReturnCourses() {
        courseRepository.save(new Course("Advanced Java", "OOP patterns", 3, "Dr. Smith"));
        List<Course> courses = courseRepository.findByInstructor("Dr. Smith");
        assertThat(courses).hasSize(2);
    }

    @Test
    void findCoursesByStudentId_shouldReturnCourses_whenStudentEnrolled() {
        Student student = new Student("Alice", "Brown", "alice.brown@test.com", 21);
        student.getCourses().add(savedCourse);
        Student savedStudent = studentRepository.save(student);

        List<Course> courses = courseRepository.findCoursesByStudentId(savedStudent.getId());
        assertThat(courses).hasSize(1);
        assertThat(courses.get(0).getTitle()).isEqualTo("Java Fundamentals");
    }

    @Test
    void findCoursesByStudentId_shouldReturnEmpty_whenNoEnrollments() {
        Student student = studentRepository.save(new Student("Bob", "Green", "bob.green@test.com", 22));
        List<Course> courses = courseRepository.findCoursesByStudentId(student.getId());
        assertThat(courses).isEmpty();
    }

    @Test
    void save_shouldPersistCourse() {
        Course course = new Course("Spring Boot", "Spring framework basics", 3, "Prof. Lee");
        Course persisted = courseRepository.save(course);
        assertThat(persisted.getId()).isNotNull();
        assertThat(courseRepository.count()).isGreaterThanOrEqualTo(2);
    }
}
