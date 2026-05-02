package com.example.studentcourse.service;

import com.example.studentcourse.dto.StudentCourseDTO;
import com.example.studentcourse.entity.Course;
import com.example.studentcourse.entity.Student;
import com.example.studentcourse.repository.CourseRepository;
import com.example.studentcourse.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    private Student student;
    private Course course;

    @BeforeEach
    void setUp() {
        course = new Course("Data Structures", "DS basics", 4, "Dr. A");
        course.setId(1L);

        student = new Student("Alice", "Smith", "alice@test.com", 21);
        student.setId(1L);
    }

    @Test
    void findAll_shouldReturnAllStudents() {
        when(studentRepository.findAll()).thenReturn(Arrays.asList(student));
        List<Student> result = studentService.findAll();
        assertThat(result).hasSize(1);
        verify(studentRepository).findAll();
    }

    @Test
    void findById_shouldReturnStudent_whenExists() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        Optional<Student> result = studentService.findById(1L);
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("alice@test.com");
    }

    @Test
    void findById_shouldReturnEmpty_whenNotExists() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<Student> result = studentService.findById(99L);
        assertThat(result).isEmpty();
    }

    @Test
    void save_shouldPersistAndReturnStudent() {
        when(studentRepository.save(student)).thenReturn(student);
        Student saved = studentService.save(student);
        assertThat(saved).isNotNull();
        verify(studentRepository).save(student);
    }

    @Test
    void update_shouldUpdateAndReturnStudent() {
        Student updated = new Student("Alice", "Johnson", "alice@test.com", 22);
        updated.setId(1L);
        updated.setCourses(new HashSet<>(Arrays.asList(course)));

        when(studentRepository.findByIdWithCourses(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(studentRepository.save(any(Student.class))).thenAnswer(inv -> inv.getArgument(0));

        Student result = studentService.update(updated);
        assertThat(result.getLastName()).isEqualTo("Johnson");
        assertThat(result.getAge()).isEqualTo(22);
    }

    @Test
    void update_shouldThrowException_whenStudentNotFound() {
        Student updated = new Student("X", "Y", "x@y.com", 20);
        updated.setId(99L);
        when(studentRepository.findByIdWithCourses(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> studentService.update(updated))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Student not found");
    }

    @Test
    void existsByEmail_shouldReturnTrue_whenEmailExists() {
        when(studentRepository.findByEmail("alice@test.com")).thenReturn(Optional.of(student));
        assertThat(studentService.existsByEmail("alice@test.com")).isTrue();
    }

    @Test
    void existsByEmail_shouldReturnFalse_whenEmailNotExists() {
        when(studentRepository.findByEmail("new@test.com")).thenReturn(Optional.empty());
        assertThat(studentService.existsByEmail("new@test.com")).isFalse();
    }

    @Test
    void existsByEmailAndIdNot_shouldReturnTrue_whenAnotherStudentHasSameEmail() {
        Student other = new Student("Bob", "Jones", "alice@test.com", 25);
        other.setId(2L);
        when(studentRepository.findByEmail("alice@test.com")).thenReturn(Optional.of(other));
        assertThat(studentService.existsByEmailAndIdNot("alice@test.com", 1L)).isTrue();
    }

    @Test
    void findAllEnrollments_shouldReturnDTOList() {
        StudentCourseDTO dto = new StudentCourseDTO("Alice", "Smith", "Data Structures", "Dr. A");
        when(studentRepository.findAllEnrollments()).thenReturn(Arrays.asList(dto));
        List<StudentCourseDTO> result = studentService.findAllEnrollments();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCourseTitle()).isEqualTo("Data Structures");
    }
}
