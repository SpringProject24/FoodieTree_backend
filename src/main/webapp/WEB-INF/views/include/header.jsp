<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

<!-- CSS : 헤더 푸터 공통-->
<link rel="stylesheet" href="/assets/css/common.css">

</head>
<body>
  <header>
    <div class="container">
      <div class="logo">FOODIE TREE</div>
      <div class="logo-img">
        <img src="/assets/img/img_2.png" alt="FoodieTree Logo">
      </div>
    <div class="wrapper-header">
        <c:if test="${login == null}">
            <div class="signin">
                <a href="/customer/sign-in" class="button btnFade btnBlueGreen">
                    Login
                </a>
            </div>
            <div class="signup">
                <a href="/customer/sign-up" class="button btnFade btnBlueGreen">
                    Signup
                </a>
            </div>
        </c:if>
        <c:if test="${login != null}">
            <div class="mypage">
                <a href="/customer/mypage" class="button btnFade btnBlueGreen">
                    Mypage
                </a>
            </div>
            <div class="signout">
                <a href="/customer/sign-out" class="button btnFade btnBlueGreen">
                    Logout
                </a>
            </div>
        </c:if>
    </div>
    </div>
  </header>
</body>
</html>