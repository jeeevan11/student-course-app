<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ include file="../common/header.jsp" %>

<div class="card">
    <div class="page-header">
        <h2>Add New Student</h2>
        <a href="${pageContext.request.contextPath}/students" class="back-link">&larr; Back to Students</a>
    </div>

    <form:form method="post" action="${pageContext.request.contextPath}/students/add" modelAttribute="student">
        <div class="form-group">
            <label for="firstName">First Name</label>
            <form:input path="firstName" id="firstName" />
            <div class="error-msg"><form:errors path="firstName" /></div>
        </div>
        <div class="form-group">
            <label for="lastName">Last Name</label>
            <form:input path="lastName" id="lastName" />
            <div class="error-msg"><form:errors path="lastName" /></div>
        </div>
        <div class="form-group">
            <label for="email">Email</label>
            <form:input path="email" id="email" type="email" />
            <div class="error-msg"><form:errors path="email" /></div>
        </div>
        <div class="form-group">
            <label for="age">Age</label>
            <form:input path="age" id="age" type="number" />
            <div class="error-msg"><form:errors path="age" /></div>
        </div>
        <div class="form-group">
            <label>Enroll in Courses</label>
            <div class="checkbox-group">
                <c:forEach var="course" items="${allCourses}">
                    <label class="checkbox-item">
                        <input type="checkbox" name="courseIds" value="${course.id}" />
                        ${course.title} (${course.credits} cr)
                    </label>
                </c:forEach>
            </div>
        </div>
        <div style="display:flex; gap:12px; margin-top:8px;">
            <button type="submit" class="btn btn-primary">Save Student</button>
            <a href="${pageContext.request.contextPath}/students" class="btn btn-secondary">Cancel</a>
        </div>
    </form:form>
</div>

<%@ include file="../common/footer.jsp" %>
