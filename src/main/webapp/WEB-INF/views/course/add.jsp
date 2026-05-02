<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ include file="../common/header.jsp" %>

<div class="card">
    <div class="page-header">
        <h2>Add New Course</h2>
        <a href="${pageContext.request.contextPath}/courses" class="back-link">&larr; Back to Courses</a>
    </div>

    <form:form method="post" action="${pageContext.request.contextPath}/courses/add" modelAttribute="course">
        <div class="form-group">
            <label for="title">Title</label>
            <form:input path="title" id="title" />
            <div class="error-msg"><form:errors path="title" /></div>
        </div>
        <div class="form-group">
            <label for="instructor">Instructor</label>
            <form:input path="instructor" id="instructor" />
            <div class="error-msg"><form:errors path="instructor" /></div>
        </div>
        <div class="form-group">
            <label for="credits">Credits</label>
            <form:input path="credits" id="credits" type="number" />
            <div class="error-msg"><form:errors path="credits" /></div>
        </div>
        <div class="form-group">
            <label for="description">Description</label>
            <form:textarea path="description" id="description" rows="4" />
            <div class="error-msg"><form:errors path="description" /></div>
        </div>
        <div style="display:flex; gap:12px; margin-top:8px;">
            <button type="submit" class="btn btn-primary">Save Course</button>
            <a href="${pageContext.request.contextPath}/courses" class="btn btn-secondary">Cancel</a>
        </div>
    </form:form>
</div>

<%@ include file="../common/footer.jsp" %>
