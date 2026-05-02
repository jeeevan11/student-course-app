package com.example.studentcourse.dto;

public class StudentCourseDTO {

    private String studentFirstName;
    private String studentLastName;
    private String courseTitle;
    private String instructor;

    public StudentCourseDTO(String studentFirstName, String studentLastName, String courseTitle, String instructor) {
        this.studentFirstName = studentFirstName;
        this.studentLastName = studentLastName;
        this.courseTitle = courseTitle;
        this.instructor = instructor;
    }

    public String getStudentFirstName() { return studentFirstName; }
    public void setStudentFirstName(String studentFirstName) { this.studentFirstName = studentFirstName; }

    public String getStudentLastName() { return studentLastName; }
    public void setStudentLastName(String studentLastName) { this.studentLastName = studentLastName; }

    public String getCourseTitle() { return courseTitle; }
    public void setCourseTitle(String courseTitle) { this.courseTitle = courseTitle; }

    public String getInstructor() { return instructor; }
    public void setInstructor(String instructor) { this.instructor = instructor; }
}
