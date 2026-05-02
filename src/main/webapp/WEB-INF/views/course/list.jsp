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
        <h2>Courses</h2>
        <a href="${pageContext.request.contextPath}/courses/add" class="btn btn-primary">+ Add Course</a>
    </div>
    <table>
        <thead>
            <tr>
                <th>#</th>
                <th>Title</th>
                <th>Instructor</th>
                <th>Credits</th>
                <th>Description</th>
                <th>Students</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="course" items="${courses}" varStatus="status">
                <tr>
                    <td>${status.count}</td>
                    <td>${course.title}</td>
                    <td>${course.instructor}</td>
                    <td>${course.credits}</td>
                    <td style="max-width:240px; font-size:13px; color:#555;">${course.description}</td>
                    <td>
                        <a href="${pageContext.request.contextPath}/courses/${course.id}/students"
                           style="color:#1a237e; font-size:13px;">${course.students.size()} enrolled</a>
                    </td>
                    <td class="actions">
                        <a href="${pageContext.request.contextPath}/courses/edit/${course.id}" class="btn btn-secondary btn-sm">Edit</a>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty courses}">
                <tr><td colspan="7" style="text-align:center; color:#999; padding:24px;">No courses found.</td></tr>
            </c:if>
        </tbody>
    </table>
</div>

<%@ include file="../common/footer.jsp" %>
