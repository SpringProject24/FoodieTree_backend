<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Footer</title>

  <!-- CSS : header, footer 공통-->
  <link rel="stylesheet" href="/assets/css/include/footer.css">

  <!-- Optional: For fading effect, include a CSS file for transitions -->
  <link rel="stylesheet" href="/assets/css/transitions.css">
</head>
<body>
<footer class="footer">
  <!-- 이미지 슬라이더 -->
  <div class="pictureDiv">
    <img id="footerImage" src="/assets/footer-img/food1.jpg" alt="Footer Image" class="footerImage">
  </div>

  <!-- 이메일 및 복사 성공 메시지 -->
  <span class="emailTitle">Based on Republic of Korea,</span>
  <span class="email" onclick="handleEmailClick()">
            foodie.treee@gmail.com
        </span>
  <span id="copySuccess" class="copySuccess" style="display: none;">Copied!</span>

  <!-- 저작권 및 링크 -->
  <span class="copyRight">©2024 FoodieTree All Rights Reserved.</span>

  <div class="footerLinks">
    <a href="#" class="terms">Terms of Service</a>
    <a href="#" class="privacy">Privacy Policy</a>
    <a href="#" class="cookies">Cookie Policy</a>
    <a href="#" class="shareDate">Do Not Sell or Share My Data</a>
    <a href="#" class="foodWaste">Food Waste Source</a>
    <a href="#" class="contact">Contact Us</a>
  </div>

  <!-- 로고 -->
  <div class="logoDiv">
    FOODIE TREE
  </div>
</footer>

<script>
  // 이미지 슬라이더 및 이메일 복사 기능
  const images = [
    '/assets/img/footer-img/food1.jpg',
    '/assets/img/footer-img/food2.jpg',
    '/assets/img/footer-img/food3.jpg',
    '/assets/img/footer-img/imagination1.jpg',
    '/assets/img/footer-img/imagination2.jpg',
    '/assets/img/footer-img/nature1.jpg',
    '/assets/img/footer-img/nature2.jpg'
  ];
  let currentImageIndex = 0;
  const footerImage = document.getElementById('footerImage');
  const copySuccess = document.getElementById('copySuccess');

  function changeImage() {
    currentImageIndex = (currentImageIndex + 1) % images.length;
    footerImage.src = images[currentImageIndex];
    footerImage.classList.add('fadeOut');
    setTimeout(() => {
      footerImage.classList.remove('fadeOut');
      footerImage.classList.add('fadeIn');
    }, 1000);
  }

  setInterval(changeImage, 4000);

  function handleEmailClick() {
    navigator.clipboard.writeText("foodie.treee@gmail.com")
            .then(() => {
              copySuccess.style.display = 'inline';
              setTimeout(() => copySuccess.style.display = 'none', 2000);
            })
            .catch(err => console.error('Failed to copy text: ', err));
  }
</script>
</body>
</html>