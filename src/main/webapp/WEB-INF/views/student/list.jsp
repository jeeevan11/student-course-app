<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="../common/header.jsp" %>

<c:if test="${not empty successMessage}">
    <div class="alert alert-success">${successMessage}</div>
</c:if>
<c:if test="${not empty errorMessage}">
    <div class="alert alert-error">${errorMessage}</div>
</c:if>

<div class="card">
    <div class="page-header">
        <h2>Students</h2>
        <a href="${pageContext.request.contextPath}/students/add" class="btn btn-primary">+ Add Student</a>
    </div>
    <table>
        <thead>
            <tr>
                <th>#</th>
                <th>Name</th>
                <th>Email</th>
                <th>Age</th>
                <th>Enrolled Courses</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="student" items="${students}" varStatus="status">
                <tr>
                    <td>${status.count}</td>
                    <td>${student.firstName} ${student.lastName}</td>
                    <td>${student.email}</td>
                    <td>${student.age}</td>
                    <td>
                        <c:forEach var="course" items="${student.courses}">
                            <span class="tag">${course.title}</span>
                        </c:forEach>
                        <c:if test="${empty student.courses}">
                            <span style="color:#999; font-size:13px;">No courses</span>
                        </c:if>
                    </td>
                    <td class="actions">
                        <a href="${pageContext.request.contextPath}/students/edit/${student.id}" class="btn btn-secondary btn-sm">Edit</a>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty students}">
                <tr><td colspan="6" style="text-align:center; color:#999; padding:24px;">No students found.</td></tr>
            </c:if>
        </tbody>
    </table>
</div>

<div class="card">
    <h2>All Enrollments (Join View)</h2>
    <table>
        <thead>
            <tr>
                <th>#</th>
                <th>Student Name</th>
                <th>Course</th>
                <th>Instructor</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="enrollment" items="${enrollments}" varStatus="status">
                <tr>
                    <td>${status.count}</td>
                    <td>${enrollment.studentFirstName} ${enrollment.studentLastName}</td>
                    <td>${enrollment.courseTitle}</td>
                    <td>${enrollment.instructor}</td>
                </tr>
            </c:forEach>
            <c:if test="${empty enrollments}">
                <tr><td colspan="4" style="text-align:center; color:#999; padding:24px;">No enrollments found.</td></tr>
            </c:if>
        </tbody>
    </table>
</div>

<%@ include file="../common/footer.jsp" %>
