package com.example.studentcourse.controller;

import com.example.studentcourse.dto.StudentCourseDTO;
import com.example.studentcourse.entity.Course;
import com.example.studentcourse.entity.Student;
import com.example.studentcourse.service.CourseService;
import com.example.studentcourse.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;
    private final CourseService courseService;

    public StudentController(StudentService studentService, CourseService courseService) {
        this.studentService = studentService;
        this.courseService = courseService;
    }

    @GetMapping
    public String listStudents(Model model) {
        List<Student> students = studentService.findAll();
        List<StudentCourseDTO> enrollments = studentService.findAllEnrollments();
        model.addAttribute("students", students);
        model.addAttribute("enrollments", enrollments);
        return "student/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("allCourses", courseService.findAll());
        return "student/add";
    }

    @PostMapping("/add")
    public String addStudent(@Valid @ModelAttribute("student") Student student,
                             BindingResult result,
                             @RequestParam(value = "courseIds", required = false) List<Long> courseIds,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("allCourses", courseService.findAll());
            return "student/add";
        }

        if (studentService.existsByEmail(student.getEmail())) {
            result.rejectValue("email", "error.student", "A student with this email already exists");
            model.addAttribute("allCourses", courseService.findAll());
            return "student/add";
        }

        if (courseIds != null && !courseIds.isEmpty()) {
            Set<Course> selectedCourses = new HashSet<>();
            for (Long courseId : courseIds) {
                courseService.findById(courseId).ifPresent(selectedCourses::add);
            }
            student.setCourses(selectedCourses);
        }

        studentService.save(student);
        redirectAttributes.addFlashAttribute("successMessage", "Student added successfully!");
        return "redirect:/students";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return studentService.findByIdWithCourses(id).map(student -> {
            model.addAttribute("student", student);
            model.addAttribute("allCourses", courseService.findAll());
            return "student/edit";
        }).orElseGet(() -> {
            redirectAttributes.addFlashAttribute("errorMessage", "Student not found");
            return "redirect:/students";
        });
    }

    @PostMapping("/edit/{id}")
    public String updateStudent(@PathVariable Long id,
                                @Valid @ModelAttribute("student") Student student,
                                BindingResult result,
                                @RequestParam(value = "courseIds", required = false) List<Long> courseIds,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("allCourses", courseService.findAll());
            return "student/edit";
        }

        if (studentService.existsByEmailAndIdNot(student.getEmail(), id)) {
            result.rejectValue("email", "error.student", "This email is already used by another student");
            model.addAttribute("allCourses", courseService.findAll());
            return "student/edit";
        }

        student.setId(id);

        if (courseIds != null && !courseIds.isEmpty()) {
            Set<Course> selectedCourses = new HashSet<>();
            for (Long courseId : courseIds) {
                courseService.findById(courseId).ifPresent(selectedCourses::add);
            }
            student.setCourses(selectedCourses);
        } else {
            student.setCourses(new HashSet<>());
        }

        studentService.update(student);
        redirectAttributes.addFlashAttribute("successMessage", "Student updated successfully!");
        return "redirect:/students";
    }
}
