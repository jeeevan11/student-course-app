package com.example.studentcourse.controller;

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
import java.util.List;

@Controller
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;
    private final StudentService studentService;

    public CourseController(CourseService courseService, StudentService studentService) {
        this.courseService = courseService;
        this.studentService = studentService;
    }

    @GetMapping
    public String listCourses(Model model) {
        List<Course> courses = courseService.findAll();
        model.addAttribute("courses", courses);
        return "course/list";
    }

    @GetMapping("/{id}/students")
    public String viewEnrolledStudents(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return courseService.findById(id).map(course -> {
            List<Student> students = studentService.findByCourseId(id);
            model.addAttribute("course", course);
            model.addAttribute("enrolledStudents", students);
            return "course/students";
        }).orElseGet(() -> {
            redirectAttributes.addFlashAttribute("errorMessage", "Course not found");
            return "redirect:/courses";
        });
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("course", new Course());
        return "course/add";
    }

    @PostMapping("/add")
    public String addCourse(@Valid @ModelAttribute("course") Course course,
                            BindingResult result,
                            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "course/add";
        }

        if (courseService.existsByTitle(course.getTitle())) {
            result.rejectValue("title", "error.course", "A course with this title already exists");
            return "course/add";
        }

        courseService.save(course);
        redirectAttributes.addFlashAttribute("successMessage", "Course added successfully!");
        return "redirect:/courses";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return courseService.findById(id).map(course -> {
            model.addAttribute("course", course);
            return "course/edit";
        }).orElseGet(() -> {
            redirectAttributes.addFlashAttribute("errorMessage", "Course not found");
            return "redirect:/courses";
        });
    }

    @PostMapping("/edit/{id}")
    public String updateCourse(@PathVariable Long id,
                               @Valid @ModelAttribute("course") Course course,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "course/edit";
        }

        if (courseService.existsByTitleAndIdNot(course.getTitle(), id)) {
            result.rejectValue("title", "error.course", "This title is already used by another course");
            return "course/edit";
        }

        course.setId(id);
        courseService.update(course);
        redirectAttributes.addFlashAttribute("successMessage", "Course updated successfully!");
        return "redirect:/courses";
    }
}
