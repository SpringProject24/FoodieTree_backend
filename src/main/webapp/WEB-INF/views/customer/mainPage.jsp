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
    <div class="wrapper">
      <div class="signin"><a href="/sign-in">로그인</a></div>
      <div class="signup"><a href="/customer/sign-up">회원가입</a></div>
    </div>
  </div>
</header>

<section class="main-body">
  <div class="main-box">
    <!-- <img src="https://images.unsplash.com/photo-1601370690183-1c7796ecec61?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D" alt="img"> -->
    <!-- <video muted autoplay loop>
      <source src="../videos/거울샷.mp4" type="video/mp4">
    </video> -->
    <img src="../assets/img/soleham.png" alt="img" class="soleham">
    <img src="../assets/img/cocojuice.png" alt="img" class="cocojuice">
    <img src="../assets/img/potato.png" alt="potato" class="potato">
    <img src="../assets/img/tomato.png" alt="tomato" class="tomato">
    <img src="../assets/img/coca.png" alt="coca" class="coca">
    <img src="../assets/img/donut.png" alt="donut" class="donut">
    <img src="../assets/img/chocolate.png" alt="chocolate" class="chocolate">
    <img src="../assets/img/strawberry.png" alt="strawberry" class="strawberry">
    
    

    <form class="main-text">    
      <h1>내 주변의<br>환경 보호에 동참하는<br>음식점을 찾아보세요!</h1>
        <input type="text" placeholder="원하는 음식점 이름 또는 주소를 입력해주세요.">
        <button type="submit">→</button>
      <div>
        <a href="/customer/sign-up" class="sign-in-btn">더 많은 혜택 누리기</a>
      </div>
    </form>
  </div>
</section>


<section class="info">
  <div class="container">
    <div class="wrapper"> 
        <h2>FOODIE TREE 입점 시 얻을 수 있는 특별한 경험에는 무엇이 있을까요?</h2>
    </div>
  </div>

  <div class="info-section">
    <div class="info-box">
      <img src="https://images.unsplash.com/photo-1490578474895-699cd4e2cf59?q=80&w=2071&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D" alt="Dasher Icon">
      <h2>음식물 낭비 최소화</h2>
      <p>철저한 음식물 관리로 낭비를 최소화하여, 환경 보호에 기여</p>
    </div>
    <div class="info-box">
      <img src="https://images.unsplash.com/photo-1533777857889-4be7c70b33f7?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D" alt="Partner Icon">
      <h2>긍정적인 지역사회 이미지</h2>
      <p>친환경적인 운영으로 지역사회에서 신뢰받고 긍정적인 이미지를 구축</p>
    </div>
    <div class="info-box">
      <img src="https://images.unsplash.com/photo-1665686377065-08ba896d16fd?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D" alt="App Icon">
      <h2>스마트한 주문 관리</h2>
      <p>첨단 기술을 활용한 주문 시스템으로 효율적인 운영과 고객 만족</p>      
    </div>
  </div>

  
  <div class="store-signup">
      <a class="storeApproval" href="/store/sign-up">입점신청</a>
  </div>
</section>

<div class="food-info">
  <div class="food-info-overlay">
    <p>FoodieTree의 목표는 환경을 보호하고 음식물 낭비를 줄이며<br>지속 가능한 식문화를 조성하는 것입니다.</p>
    <h2>고객 여러분도 FoodieTree와 함께 환경 보호에 동참해 주세요.</h2>
  </div>
</div>

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
