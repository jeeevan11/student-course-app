package com.example.studentcourse.service;

import com.example.studentcourse.dto.StudentCourseDTO;
import com.example.studentcourse.entity.Course;
import com.example.studentcourse.entity.Student;
import com.example.studentcourse.repository.CourseRepository;
import com.example.studentcourse.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public StudentServiceImpl(StudentRepository studentRepository, CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Student> findByIdWithCourses(Long id) {
        return studentRepository.findByIdWithCourses(id);
    }

    @Override
    public Student save(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Student update(Student student) {
        Student existing = studentRepository.findByIdWithCourses(student.getId())
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + student.getId()));

        existing.setFirstName(student.getFirstName());
        existing.setLastName(student.getLastName());
        existing.setEmail(student.getEmail());
        existing.setAge(student.getAge());

        if (student.getCourses() != null && !student.getCourses().isEmpty()) {
            Set<Course> managedCourses = new HashSet<>();
            for (Course c : student.getCourses()) {
                courseRepository.findById(c.getId()).ifPresent(managedCourses::add);
            }
            existing.setCourses(managedCourses);
        } else {
            existing.setCourses(new HashSet<>());
        }

        return studentRepository.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return studentRepository.findByEmail(email).isPresent();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmailAndIdNot(String email, Long id) {
        Optional<Student> found = studentRepository.findByEmail(email);
        return found.isPresent() && !found.get().getId().equals(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> findByCourseId(Long courseId) {
        return studentRepository.findStudentsByCourseId(courseId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentCourseDTO> findAllEnrollments() {
        return studentRepository.findAllEnrollments();
    }
}
