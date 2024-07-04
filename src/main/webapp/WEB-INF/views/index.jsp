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
  <!-- 구글폰트 -->
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100..900&display=swap" rel="stylesheet">
  <!-- 구글폰트2 -->
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Gaegu&display=swap" rel="stylesheet">
  <!-- 아이콘 -->
  <!-- Add the following <link> to the <head> of your HTML. -->
  <!-- <link rel="stylesheet" href="https://cdn.linearicons.com/free/1.0.0/icon-font.min.css"> -->
</head>
<body>
   <header>
    <div class="container">
      <div class="logo">FOODIE TREE</div>
      <div class="logo-img">
        <img src="/assets/img/img_2.png" alt="FoodieTree Logo">
      </div>
      <div class="wrapper">
        <c:if test="${login == null}">
            <div class="signin">
                <a href="/customer/sign-in" class="button btnFade btnBlueGreen">
                    로그인
                </a>
            </div>
            <div class="signup">
                <a href="/customer/sign-up" class="button btnFade btnBlueGreen">
                    회원가입
                </a>
            </div>
        </c:if>
        <c:if test="${login != null}">
            <div class="mypage">
                <a href="/customer/mypage" class="button btnFade btnBlueGreen">
                    마이페이지
                </a>
            </div>
            <div class="signout">
                <a href="/customer/sign-out" class="button btnFade btnBlueGreen">
                    로그아웃
                </a>
            </div>
        </c:if>
    </div>
    
    </div>
  </header>
<section class="hero">
  <div class="container">
    <h2>환경 보호에 동참하는</h2>
    <p>우리 주변의 음식점들을 찾아보세요!</p>
    <div class="search-wrapper">
      <div class="search">
        <span>>>> 여기에 원하는 음식점 이름 또는 주소를 입력하세요!</span>
      </div>
      <div class="sub-signup"><a href="#">Foodie Tree 회원이 되어 더 많은 혜택을 누려보세요!</a></div>
    </div>
  </div>
</section>
<section class="info">
  <div class="container">
    <div class="left">
      <div class="wrapper">
        <h2>FOODIE TREE 입점 시 얻을 수 있는 특별한 경험에는 무엇이 있을까요?</h2>
        <p>우리 가게에서 판매하고 버려지는 음식물을 최소화하여 환경을 보호하고</p>
        <p>지역사회에 우리 가게에 대해 긍정적인 이미지를 심어줄 수 있어요!</p>
      </div>
    </div>
    <div class="right">
      <div class="store-signup">
        <a href="/store/sign-up"><span>입점신청</span></a>
      </div>
    </div>
  </div>
</section>
<footer>
  <div class="container">
    <div class="big-title">FOODIE TREE</div>
    <div class="custom_link_all">
      <a href="#" title="Legal">
        <p>Legal</p>
      </a>
      <a href="#" title="Privacy Policy">
        <p class="custom_link_text">Privacy Policy</p>
      </a>
      <a href="#" title="Cookie Policy">
        <p class="custom_link_text">Cookie Policy</p>
      </a>
      <a href="#" title="Terms & Conditions">
        <p class="custom_link_text">Terms & Conditions</p>
      </a>
      <a href="#" title="Contact us">
        <p class="custom_link_text">Contact us</p>
      </a>
      <a href="#" title="DSA Disclosure">
        <p class="custom_link_text">DSA Disclosure</p>
      </a>
      <a href="#" title="Food Waste Sources">
        <p class="custom_link_text">Food Waste Sources</p>
      </a>
    </div>
    <p class="footer-text">&copy; 2024 FoodieTree. All rights reserved.</p>
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