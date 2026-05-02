package com.example.studentcourse.config;

import com.example.studentcourse.entity.Course;
import com.example.studentcourse.entity.Student;
import com.example.studentcourse.repository.CourseRepository;
import com.example.studentcourse.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public DataInitializer(StudentRepository studentRepository, CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (courseRepository.count() > 0) return;

        Course c1 = courseRepository.save(new Course("Data Structures", "Covers arrays, linked lists, trees, and graphs.", 4, "Dr. Alice Johnson"));
        Course c2 = courseRepository.save(new Course("Operating Systems", "Process management, memory, and file systems.", 4, "Prof. Bob Martinez"));
        Course c3 = courseRepository.save(new Course("Database Systems", "Relational databases, SQL, and query optimization.", 3, "Dr. Carol Lee"));
        Course c4 = courseRepository.save(new Course("Computer Networks", "TCP/IP, routing protocols, and network security.", 3, "Prof. David Kim"));
        Course c5 = courseRepository.save(new Course("Software Engineering", "SDLC, design patterns, and agile practices.", 3, "Dr. Emily Chen"));
        Course c6 = courseRepository.save(new Course("Machine Learning", "Supervised and unsupervised learning algorithms.", 4, "Prof. Frank Wilson"));
        Course c7 = courseRepository.save(new Course("Web Development", "HTML, CSS, JavaScript, and REST APIs.", 3, "Dr. Grace Brown"));
        Course c8 = courseRepository.save(new Course("Algorithms", "Sorting, searching, and complexity analysis.", 4, "Prof. Henry Davis"));
        Course c9 = courseRepository.save(new Course("Cloud Computing", "AWS, Azure, and distributed system design.", 3, "Dr. Iris Taylor"));
        Course c10 = courseRepository.save(new Course("Cybersecurity", "Encryption, vulnerability analysis, and ethics.", 3, "Prof. James Anderson"));

        List<Course> allCourses = Arrays.asList(c1, c2, c3, c4, c5, c6, c7, c8, c9, c10);

        Student s1 = new Student("Arjun", "Sharma", "arjun.sharma@example.com", 20);
        s1.getCourses().addAll(Arrays.asList(c1, c3, c5));

        Student s2 = new Student("Priya", "Patel", "priya.patel@example.com", 21);
        s2.getCourses().addAll(Arrays.asList(c2, c4, c6));

        Student s3 = new Student("Ravi", "Kumar", "ravi.kumar@example.com", 22);
        s3.getCourses().addAll(Arrays.asList(c1, c2, c7));

        Student s4 = new Student("Sneha", "Nair", "sneha.nair@example.com", 19);
        s4.getCourses().addAll(Arrays.asList(c3, c8, c9));

        Student s5 = new Student("Karan", "Mehta", "karan.mehta@example.com", 23);
        s5.getCourses().addAll(Arrays.asList(c5, c6, c10));

        Student s6 = new Student("Anjali", "Singh", "anjali.singh@example.com", 20);
        s6.getCourses().addAll(Arrays.asList(c4, c7, c8));

        Student s7 = new Student("Vikram", "Rao", "vikram.rao@example.com", 24);
        s7.getCourses().addAll(Arrays.asList(c1, c6, c9));

        Student s8 = new Student("Divya", "Gupta", "divya.gupta@example.com", 21);
        s8.getCourses().addAll(Arrays.asList(c2, c5, c10));

        Student s9 = new Student("Rohit", "Joshi", "rohit.joshi@example.com", 22);
        s9.getCourses().addAll(Arrays.asList(c3, c7, c8));

        Student s10 = new Student("Meera", "Iyer", "meera.iyer@example.com", 20);
        s10.getCourses().addAll(Arrays.asList(c4, c6, c9));

        Student s11 = new Student("Aditya", "Bansal", "aditya.bansal@example.com", 23);
        s11.getCourses().addAll(Arrays.asList(c1, c5, c10));

        Student s12 = new Student("Nisha", "Kapoor", "nisha.kapoor@example.com", 19);
        s12.getCourses().addAll(Arrays.asList(c2, c3, c4));

        studentRepository.saveAll(Arrays.asList(s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12));
    }
}
