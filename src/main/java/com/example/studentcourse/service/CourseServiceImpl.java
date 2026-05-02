package com.example.studentcourse.service;

import com.example.studentcourse.entity.Course;
import com.example.studentcourse.repository.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Course> findById(Long id) {
        return courseRepository.findById(id);
    }

    @Override
    public Course save(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public Course update(Course course) {
        Course existing = courseRepository.findById(course.getId())
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + course.getId()));

        existing.setTitle(course.getTitle());
        existing.setDescription(course.getDescription());
        existing.setCredits(course.getCredits());
        existing.setInstructor(course.getInstructor());

        return courseRepository.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByTitle(String title) {
        return courseRepository.findByTitle(title).isPresent();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByTitleAndIdNot(String title, Long id) {
        Optional<Course> found = courseRepository.findByTitle(title);
        return found.isPresent() && !found.get().getId().equals(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Course> findByStudentId(Long studentId) {
        return courseRepository.findCoursesByStudentId(studentId);
    }
}
