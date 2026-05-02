<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="../common/header.jsp" %>

<div class="card">
    <div class="page-header">
        <h2>Students enrolled in: ${course.title}</h2>
        <a href="${pageContext.request.contextPath}/courses" class="back-link">&larr; Back to Courses</a>
    </div>
    <p style="margin-bottom:16px; font-size:14px; color:#555;">
        Instructor: <strong>${course.instructor}</strong> &nbsp;|&nbsp; Credits: <strong>${course.credits}</strong>
    </p>
    <table>
        <thead>
            <tr>
                <th>#</th>
                <th>Name</th>
                <th>Email</th>
                <th>Age</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="student" items="${enrolledStudents}" varStatus="status">
                <tr>
                    <td>${status.count}</td>
                    <td>${student.firstName} ${student.lastName}</td>
                    <td>${student.email}</td>
                    <td>${student.age}</td>
                </tr>
            </c:forEach>
            <c:if test="${empty enrolledStudents}">
                <tr><td colspan="4" style="text-align:center; color:#999; padding:24px;">No students enrolled in this course.</td></tr>
            </c:if>
        </tbody>
    </table>
</div>

<%@ include file="../common/footer.jsp" %>
