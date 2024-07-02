<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
  <header>
    <div class="container">
      <div class="logo">FoodieTree</div>
      <div class="logo-img">
        <img src="/assets/img/img_2.png" alt="">
      </div>
      <div>
        <c:if test="${login == null}">
          <li><a href="/customer/sign-up">Sign Up</a></li>
          <li><a href="/customer/sign-in">Sign In</a></li>
        </c:if>
      
        <c:if test="${login != null}">
          <li><a href="#">My Page</a></li>
          <li><a href="/customer/sign-out">Sign Out</a></li>
        </c:if>
    </div>
      <div class="wrapper">
        <div class="signin"><a href="/sign-in">로그인</a></div>
        <div class="signup"><a href="/customer/sign-up">회원가입</a></div>
      </div>
    </div>
  </header>
</body>
</html>