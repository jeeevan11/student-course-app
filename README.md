# Student Course Management App

A Spring Boot web app for managing students and their course enrollments. Built with Spring MVC, Spring Data JPA, JSP views, and an H2 in-memory database. Supports creating, listing, and editing both students and courses, plus a join-based enrollment view.

---

## Tech Stack

Spring Boot 2.7 / Spring MVC / Spring Data JPA / JSP + JSTL / H2 / JUnit 5 + Mockito

---

## Project Structure

```
src/main/java/com/example/studentcourse/
    StudentCourseApplication.java
    config/
        DataInitializer.java          <- seeds 10+ students and courses on startup
    entity/
        Student.java                  <- ManyToMany owner
        Course.java                   <- inverse side
    dto/
        StudentCourseDTO.java         <- projection for join query
    repository/
        StudentRepository.java        <- custom JPQL queries
        CourseRepository.java
    service/
        StudentService.java
        StudentServiceImpl.java
        CourseService.java
        CourseServiceImpl.java
    controller/
        HomeController.java
        StudentController.java
        CourseController.java

src/main/webapp/WEB-INF/views/
    common/header.jsp  footer.jsp
    student/list.jsp  add.jsp  edit.jsp
    course/list.jsp   add.jsp  edit.jsp  students.jsp

src/test/java/com/example/studentcourse/
    repository/StudentRepositoryTest.java
    repository/CourseRepositoryTest.java
    service/StudentServiceTest.java
    service/CourseServiceTest.java
```

---

## Running the App

```bash
git clone <repo-url>
cd student-course-app
mvn spring-boot:run
```

Open http://localhost:8080 in your browser. The app redirects to the students list, which already has 12 students and 10 courses loaded from the initializer.

H2 console is available at http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:mem:studentcoursedb`, no password).

---

## Running Tests

```bash
mvn test
```

33 tests across 4 test classes. All pass. No manual setup needed.

---

## Features

**Students list** shows all students with their enrolled courses as tags. A second table on the same page shows the full join view (student name + course + instructor), powered by a JPQL inner join query.

**Add / Edit Student** includes a checkbox list of all courses for enrollment management. Duplicate email validation prevents bad data.

**Courses list** shows each course with a clickable enrollment count. Clicking through runs a second custom JPQL join query to pull only students enrolled in that specific course.

**Add / Edit Course** standard form with validation on all fields.

---

## Entity Design

Student and Course share a ManyToMany relationship through a join table called `student_courses`. Student is the owning side and holds the `@JoinTable` definition. Course holds `mappedBy = "courses"`.

This means enrollment is always managed through the Student side. When you save or update a student, you pass the full set of course IDs and the service resolves them into managed entities before persisting. The Course side is read-only from JPA's perspective.

Key fields:

Student: id, firstName, lastName, email (unique), age

Course: id, title (unique), description, credits (1-6), instructor

---

## Repository Queries

The interesting ones are the custom JPQL joins:

```java
@Query("SELECT s FROM Student s JOIN s.courses c WHERE c.id = :courseId")
List<Student> findStudentsByCourseId(@Param("courseId") Long courseId);

@Query("SELECT new com.example.studentcourse.dto.StudentCourseDTO(s.firstName, s.lastName, c.title, c.instructor) " +
       "FROM Student s JOIN s.courses c ORDER BY s.lastName, s.firstName")
List<StudentCourseDTO> findAllEnrollments();
```

The DTO projection approach avoids fetching full entities when you only need a flat row for display. The inner join means only students with at least one enrollment show up in that second table.

---

## Known Considerations

The H2 database resets on every restart. That's intentional given the `create-drop` DDL strategy. To use MySQL, swap the datasource properties in `application.properties` and add the MySQL driver dependency to `pom.xml`.

Bean validation is applied at the controller layer via `@Valid`. Duplicate email and title checks are handled manually in the service layer rather than relying on DB constraint exceptions, so the user gets a clean inline error message instead of a stack trace page.

The `DataInitializer` runs inside a `@Transactional` method. This keeps all the seeded entities in the same persistence context, which avoids detached-entity errors when associating already-saved courses with new students.
