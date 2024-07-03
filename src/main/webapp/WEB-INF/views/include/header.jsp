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
        <img src="/assets/img/img_2.png" alt="FoodieTree Logo">
      </div>
      <div class="wrapper">
        <c:if test="${login == null}">
          <div class="signin"><a href="/customer/sign-in">로그인</a></div>
          <div class="signup"><a href="/customer/sign-up">회원가입</a></div>
        </c:if>
        <c:if test="${login != null}">
          <div class="mypage"><a href="/customer/mypage">마이페이지</a></div>
          <div class="signout"><a href="/customer/sign-out">로그아웃</a></div>
        </c:if>
      </div>
    </div>
  </header>
</body>
</html>