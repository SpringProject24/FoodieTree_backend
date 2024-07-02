<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>FoodieTree</title>
  <link rel="stylesheet" href="/assets/css/common.css">
  <link rel="stylesheet" href="/assets/css/index.css">
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
        <li><a href="#">Sign Out</a></li>
      </c:if>
  </div>
    <div class="wrapper">
      <div class="signin"><a href="/sign-in">로그인</a></div>
      <div class="signup"><a href="/customer/sign-up">회원가입</a></div>
    </div>
  </div>
</header>

<section class="hero">
  <div class="container">
    <h2>환경 보호에 동참하는</h2>
    <p>내 주변의 음식점을 찾아보세요!</p>
    <div class="search-wrapper">
      <div class="search">
        <span>원하는 음식점 이름 또는 주소를 입력하세요!</span>
      </div>
      <div class="sub-signup"><a href="#">회원이 되어 더 많은 혜택을 누려보세요</a></div>
    </div>
  </div>
</section>

<section class="info">
  <div class="container">
    <div class="left">
      <div class="wrapper">
        <h2>FOODIE TREE 입점 시 얻을 수 있는 특별한 경험에는 무엇이 있을까요?</h2>
        <p>우리 가게에 버리는 음식물을 최소화 하여 환경을 보호하고</p>
        <p>지역사회에  우리 가게에 대해 긍정적인 이미지를 심어줄 수 있어요!</p>
      </div>
    </div>
    <div class="right">
      <div class="store-signup"><a href="/store/sign-up">입점신청</a></div>
    </div>
  </div>
</section>

<footer>
  <div class="container">
    <p>&copy; 2024 FoodieTree. All rights reserved.</p>
  </div>
</footer>
  <script>
  const $storeSignup = document.querySelector('.store-signup');
    $storeSignup.addEventListener('click', () => {
        location.href = '/store/sign-up';
    });
</script>
</body>
</html>
