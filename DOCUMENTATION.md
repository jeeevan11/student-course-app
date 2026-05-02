# Technical Documentation


## Entity Relationship Design

The two entities are Student and Course. A student can enroll in multiple courses, and a course can have multiple students, so the relationship is ManyToMany. The join table is `student_courses` with columns `student_id` and `course_id`.

Student is the owning side because enrollment is always initiated from the student's perspective. The `@JoinTable` annotation sits on the `courses` field inside Student. Course has `mappedBy = "courses"` which tells JPA to look at the Student side for the actual join table definition.

Fetch strategy on both sides is `LAZY`. This prevents Hibernate from pulling every associated entity on every query, which matters once the data grows. The tradeoff is that you have to be intentional about when you actually need the related collection. The edit form for students calls `findByIdWithCourses`, which uses a `LEFT JOIN FETCH` to hydrate the courses in a single query instead of triggering N+1 loads.


## Create Operation

For students, the form POSTs to `/students/add`. The controller method receives the Student object via `@ModelAttribute` and a separate `courseIds` list from the checkbox group. Spring's binding handles the scalar fields automatically but cannot bind ManyToMany collections from checkboxes directly, so the controller manually fetches each Course by ID and adds it to the student's courses set before calling `save`.

Validation runs in two stages. First, `@Valid` triggers bean validation constraints on the Student fields (not blank, email format, age range). If those fail, the form re-renders with inline error messages from `<form:errors>`. If they pass, the service checks for email uniqueness against the database. This prevents the unique constraint violation from bubbling up as an ugly 500 page.

For courses the process is the same minus the enrollment handling. Title uniqueness is checked at the service layer.

The `DataInitializer` seeds 12 students and 10 courses when the app starts. It's wrapped in `@Transactional` so all the entities live in the same persistence context. Without that, the courses are detached by the time Hibernate tries to cascade-persist them through the students, which throws a `PersistentObjectException`.


## Read Operation

The students list page runs two queries. The first is a plain `findAll()` to get all students with their basic fields. The second is the custom DTO projection query:

```java
SELECT new com.example.studentcourse.dto.StudentCourseDTO(s.firstName, s.lastName, c.title, c.instructor)
FROM Student s JOIN s.courses c ORDER BY s.lastName, s.firstName
```

This is an INNER JOIN, so only students with at least one enrolled course appear in the second table. The DTO constructor receives four scalar values directly from the projection rather than loading full entity graphs, which keeps memory usage flat regardless of how many courses a student has.

The courses list page shows a clickable enrollment count per course. Clicking it hits `/courses/{id}/students` and runs a separate join query:

```java
SELECT s FROM Student s JOIN s.courses c WHERE c.id = :courseId
```

This returns only the students enrolled in that specific course, again using the join table.


## Update Operation

The edit flow for both entities follows the same pattern. On GET, the controller loads the entity and puts it into the model. For students, `findByIdWithCourses` is used so the JSP can pre-check the enrolled courses in the checkbox list. The JSP uses a nested `<c:forEach>` to compare each available course ID against the student's enrolled courses set and marks matching checkboxes as checked.

On POST, the service's `update` method loads the existing entity fresh from the database before applying changes. This avoids stale state and makes sure the changes are applied to a managed entity. For students, the service resolves the submitted course IDs into managed Course objects before setting them on the student, preventing detachment issues.

Email and title uniqueness checks on update use the `existsByEmailAndIdNot` and `existsByTitleAndIdNot` variants so the current record's own value doesn't trigger a false duplicate error.


## Code Snippets


Custom JPQL enrollment DTO query:

```java
@Query("SELECT new com.example.studentcourse.dto.StudentCourseDTO(s.firstName, s.lastName, c.title, c.instructor) " +
       "FROM Student s JOIN s.courses c ORDER BY s.lastName, s.firstName")
List<StudentCourseDTO> findAllEnrollments();
```


Service-layer update with managed entity resolution:

```java
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
```


Controller handling duplicate email on create:

```java
if (studentService.existsByEmail(student.getEmail())) {
    result.rejectValue("email", "error.student", "A student with this email already exists");
    model.addAttribute("allCourses", courseService.findAll());
    return "student/add";
}
```


## Challenges and Solutions

The detached entity error on startup was the first real issue. When `saveAll` runs on students and `CascadeType.PERSIST` is active, Hibernate tries to re-persist the course objects that were already saved in a previous call. They're detached at that point because each `save` for a course creates and closes its own transaction. Wrapping the entire `DataInitializer.run` method in `@Transactional` fixes it because all entities stay in the same session throughout the initialization.

Pre-checking enrolled courses in the edit form required a JSTL workaround. Spring's `<form:checkboxes>` tag has trouble with ManyToMany collections of complex objects when the form binding is for a different field name (`courseIds` vs the entity's `courses`). The solution was to use plain HTML checkboxes with a nested `<c:forEach>` that does a manual ID comparison against the student's course set.

The JPQL DTO constructor expression requires the fully qualified class name in the query string. Shortening it is not supported in JPQL, so the full package path stays in the annotation. Not pretty but it works and there is no runtime overhead since the query is compiled once.


## Test Coverage

Repository tests use `@DataJpaTest` which spins up a real H2 schema and JPA context but excludes the rest of the application. This tests the actual SQL and ORM behavior rather than mocking it away.

Service tests use `@ExtendWith(MockitoExtension.class)` and mock both repositories. They cover the core business logic paths: happy paths, not-found cases, duplicate detection, and the entity resolution logic inside `update`.

33 tests total, all passing.
