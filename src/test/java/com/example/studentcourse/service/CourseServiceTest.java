package com.example.studentcourse.service;

import com.example.studentcourse.entity.Course;
import com.example.studentcourse.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseServiceImpl courseService;

    private Course course;

    @BeforeEach
    void setUp() {
        course = new Course("Algorithms", "Sorting and searching", 4, "Prof. B");
        course.setId(1L);
    }

    @Test
    void findAll_shouldReturnAllCourses() {
        when(courseRepository.findAll()).thenReturn(Arrays.asList(course));
        List<Course> result = courseService.findAll();
        assertThat(result).hasSize(1);
        verify(courseRepository).findAll();
    }

    @Test
    void findById_shouldReturnCourse_whenExists() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        Optional<Course> result = courseService.findById(1L);
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Algorithms");
    }

    @Test
    void findById_shouldReturnEmpty_whenNotFound() {
        when(courseRepository.findById(50L)).thenReturn(Optional.empty());
        assertThat(courseService.findById(50L)).isEmpty();
    }

    @Test
    void save_shouldPersistAndReturnCourse() {
        when(courseRepository.save(course)).thenReturn(course);
        Course saved = courseService.save(course);
        assertThat(saved).isNotNull();
        verify(courseRepository).save(course);
    }

    @Test
    void update_shouldUpdateCourseFields() {
        Course updated = new Course("Algorithms Advanced", "Graph algorithms", 4, "Prof. C");
        updated.setId(1L);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseRepository.save(any(Course.class))).thenAnswer(inv -> inv.getArgument(0));

        Course result = courseService.update(updated);
        assertThat(result.getTitle()).isEqualTo("Algorithms Advanced");
        assertThat(result.getInstructor()).isEqualTo("Prof. C");
    }

    @Test
    void update_shouldThrowException_whenCourseNotFound() {
        Course updated = new Course("Ghost", "...", 2, "Nobody");
        updated.setId(99L);
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> courseService.update(updated))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Course not found");
    }

    @Test
    void existsByTitle_shouldReturnTrue_whenExists() {
        when(courseRepository.findByTitle("Algorithms")).thenReturn(Optional.of(course));
        assertThat(courseService.existsByTitle("Algorithms")).isTrue();
    }

    @Test
    void existsByTitle_shouldReturnFalse_whenNotExists() {
        when(courseRepository.findByTitle("Unknown")).thenReturn(Optional.empty());
        assertThat(courseService.existsByTitle("Unknown")).isFalse();
    }

    @Test
    void existsByTitleAndIdNot_shouldReturnTrue_whenDifferentCourseHasSameTitle() {
        Course other = new Course("Algorithms", "Another one", 3, "Someone");
        other.setId(2L);
        when(courseRepository.findByTitle("Algorithms")).thenReturn(Optional.of(other));
        assertThat(courseService.existsByTitleAndIdNot("Algorithms", 1L)).isTrue();
    }

    @Test
    void existsByTitleAndIdNot_shouldReturnFalse_whenSameCourseId() {
        when(courseRepository.findByTitle("Algorithms")).thenReturn(Optional.of(course));
        assertThat(courseService.existsByTitleAndIdNot("Algorithms", 1L)).isFalse();
    }
}
