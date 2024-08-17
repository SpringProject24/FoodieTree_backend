<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>FoodieTree</title>
    <link rel="stylesheet" href="/assets/css/index.css" />
    <link rel="stylesheet" href="/assets/css/store/guest-storelist.css">
    <!-- 구글폰트 -->
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link
      href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100..900&display=swap"
      rel="stylesheet"
    />
    <!-- 구글폰트2 -->
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link
      href="https://fonts.googleapis.com/css2?family=Gaegu&display=swap"
      rel="stylesheet"
    />

<%--     system font--%>
    <style>
      :root {
        font-family: Inter, Avenir, Helvetica, Arial, sans-serif;
        font-size: 16px;
        line-height: 24px;
        font-weight: 400;
        font-synthesis: none;
        text-rendering: optimizeLegibility;
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
        -webkit-text-size-adjust: 100%;
      }
    </style>
  </head>


<%--  body start--%>
  <body>
  <!-- 공통헤더 -->
  <%@ include file="include/header.jsp" %>

  <section class="hero">
      <div class="main-box">
<%--              로고--%>
              <div class="main-logo">
            <img src="${pageContext.request.contextPath}/assets/img/icon/greenlogo.png" alt="Green Foodie Tree Logo">
              </div>
<%--    save the earth--%>
            <div class="save-quote">
            <img src="${pageContext.request.contextPath}/assets/img/main-quote/greensave.png" alt="Save The Earth">
            </div>
<%--        savor the taste--%>
            <div class="savor-quote">
            <img src="${pageContext.request.contextPath}/assets/img/main-quote/greenlettersavor.png" alt="Savor The Taste">
            </div>
      </div>
  </section>

  <section class="info">
      <div class="container">
          <div class="left">
              <div class="wrapper jua-regular">
                  <h2><span class="margarine-regular">FoodieTree</span> 입점 시 얻을 수 있는 특별한 경험에는 무엇이 있을까요?</h2>
                  <p>우리 가게에서 판매하고 버려지는 음식물을 최소화하여 환경을 보호하고</p>
                  <p>지역사회에 우리 가게에 대해 긍정적인 이미지를 심어줄 수 있어요!</p>
              </div>
          </div>
          <div class="right">
              <div class="store-signup jua-regular">
                  <a href="/store/sign-up"><span>입점신청</span></a>
              </div>
          </div>
      </div>
  </section>

  <section class="guest-store-list-section">
<%--      <%@ include file="guestProductPage.jsp" %>--%>
  </section>

  <section class="info-section">
      <div class="info-box">
          <h2>음식물 낭비 최소화</h2>
          <p>철저한 음식물 관리로 낭비를 최소화하여, 환경 보호에 기여</p>
      </div>
      <div class="info-box">
          <h2>긍정적인 지역사회 이미지</h2>
          <p>친환경적인 운영으로 지역사회에서 신뢰받고 긍정적인 이미지를 구축</p>
      </div>
      <div class="info-box">
          <h2>스마트한 주문 관리</h2>
          <p>첨단 기술을 활용한 주문 시스템으로 효율적인 운영과 고객 만족</p>
      </div>
  </section>

  <section class="food-info">
      <div class="food-info-overlay">
          <p>FoodieTree의 목표는 환경을 보호하고 음식물 낭비를 줄이며<br />지속 가능한 식문화를 조성하는 것입니다.</p>
          <h2>고객 여러분도 FoodieTree와 함께 환경 보호에 동참해 주세요.</h2>
      </div>
  </section>

  <!-- 공통 푸터 -->
  <footer>
      <%@ include file="include/footer.jsp" %>
  </footer>

  <script>
      const $storeSignup = document.querySelector(".store-signup");
      $storeSignup.addEventListener("click", () => {
          location.href = "/store/sign-up";
      });

      document.addEventListener('click', function(event) {
          // Define an array of emojis to use
          const emojis = ['🍃', '🌿', '🍀', '🍂', '🌱'];

          // Create and animate leaves
          for (let i = 0; i < 10; i++) { // Adjust the number of leaves
              const leaf = document.createElement('div');
              leaf.classList.add('leaf');
              leaf.textContent = emojis[Math.floor(Math.random() * emojis.length)]; // Random emoji
              leaf.style.left = `${event.clientX + (Math.random() * 20 - 10)}px`; // Randomize position around click
              leaf.style.top = `${event.clientY + (Math.random() * 20 - 10)}px`; // Randomize position around click
              leaf.style.opacity = Math.random() * 0.5 + 0.5; // Random opacity
              document.body.appendChild(leaf);

              // Remove the leaf after animation
              leaf.addEventListener('animationend', function() {
                  leaf.remove();
              });
          }
      });
  </script>


  </body>
<%--    body end point--%>

</html>
