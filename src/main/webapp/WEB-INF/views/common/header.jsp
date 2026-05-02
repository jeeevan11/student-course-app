<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Course Management</title>
    <style>
        * { box-sizing: border-box; margin: 0; padding: 0; }
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: #f0f2f5; color: #333; }
        nav { background: #1a237e; padding: 0 30px; display: flex; align-items: center; gap: 20px; height: 56px; }
        nav a { color: #fff; text-decoration: none; font-size: 15px; padding: 8px 14px; border-radius: 4px; transition: background 0.2s; }
        nav a:hover, nav a.active { background: rgba(255,255,255,0.18); }
        nav .brand { font-size: 18px; font-weight: 700; margin-right: 20px; letter-spacing: 0.5px; }
        .container { max-width: 1100px; margin: 30px auto; padding: 0 20px; }
        .card { background: #fff; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.08); padding: 28px; margin-bottom: 24px; }
        h2 { font-size: 22px; margin-bottom: 20px; color: #1a237e; }
        .alert { padding: 12px 18px; border-radius: 5px; margin-bottom: 18px; font-size: 14px; }
        .alert-success { background: #e8f5e9; color: #2e7d32; border-left: 4px solid #43a047; }
        .alert-error { background: #ffebee; color: #c62828; border-left: 4px solid #e53935; }
        table { width: 100%; border-collapse: collapse; font-size: 14px; }
        th { background: #e8eaf6; color: #1a237e; padding: 12px 14px; text-align: left; font-weight: 600; }
        td { padding: 11px 14px; border-bottom: 1px solid #eeeeee; }
        tr:last-child td { border-bottom: none; }
        tr:hover td { background: #f5f5f5; }
        .btn { display: inline-block; padding: 9px 20px; border-radius: 5px; font-size: 14px; font-weight: 500; text-decoration: none; border: none; cursor: pointer; transition: background 0.2s, opacity 0.2s; }
        .btn-primary { background: #1a237e; color: #fff; }
        .btn-primary:hover { background: #283593; }
        .btn-secondary { background: #546e7a; color: #fff; }
        .btn-secondary:hover { background: #455a64; }
        .btn-sm { padding: 5px 13px; font-size: 13px; }
        .form-group { margin-bottom: 18px; }
        label { display: block; font-size: 14px; font-weight: 500; margin-bottom: 6px; color: #444; }
        input[type=text], input[type=email], input[type=number], textarea, select { width: 100%; padding: 9px 12px; border: 1px solid #ccc; border-radius: 5px; font-size: 14px; transition: border-color 0.2s; }
        input:focus, textarea:focus, select:focus { outline: none; border-color: #1a237e; }
        .error-msg { color: #e53935; font-size: 13px; margin-top: 4px; }
        .tag { display: inline-block; background: #e8eaf6; color: #1a237e; border-radius: 12px; padding: 3px 10px; font-size: 12px; margin: 2px 2px 2px 0; }
        .actions { display: flex; gap: 8px; }
        .checkbox-group { display: flex; flex-wrap: wrap; gap: 10px; }
        .checkbox-item { display: flex; align-items: center; gap: 6px; background: #f5f5f5; padding: 6px 12px; border-radius: 4px; font-size: 13px; cursor: pointer; }
        .checkbox-item input[type=checkbox] { width: auto; }
        .page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
        .back-link { color: #546e7a; text-decoration: none; font-size: 14px; }
        .back-link:hover { color: #1a237e; }
    </style>
</head>
<body>
<nav>
    <span class="brand">SCM App</span>
    <a href="${pageContext.request.contextPath}/students">Students</a>
    <a href="${pageContext.request.contextPath}/courses">Courses</a>
</nav>
<div class="container">
