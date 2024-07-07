<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FoodieTree</title>
    <link rel="stylesheet" href="/assets/css/common.css">
    <link rel="stylesheet" href="/assets/css/store/sign-up.css">
</head>
<body>
  <header>
      <div class="container">
          <div class="logo margarine-regular">FoodieTree</div>
          <div class="logo-img">
              <img src="/assets/img/img_2.png" alt="Logo">
          </div>
      </div>
  </header>
<section class="input-area">
    <form action="/store/sign-up" method="post">
        <div class="container">
            <div class="id-wrapper">
                <h2>회원 등록을 위한 이메일을 입력해주세요!</h2>
                <input type="text" name="account" placeholder="이메일을 입력해주세요">
                <button id="id-btn">계속</button>
            </div>
            <div class="pass-wrapper none">
                <div class="pass">
                    <h2>계속해서 비밀번호를 입력해주세요!</h2>
                    <input type="password" name="password" placeholder="비밀번호를 입력해주세요">
                </div>
                <div class="pass-check">
                    <input type="password" name="password-chk" placeholder="비밀번호를 확인해주세요">
                    <div class="wrapper">
                        <button id="prev-btn">이전</button>
                        <button id="pass-btn">계속</button>
                    </div>
                </div>
            </div>
        </div>
    </form>
</section>

<script>
  const $idBtn = document.getElementById('id-btn');
  const $passBtn = document.getElementById('pass-btn');
  const $idWrapper = document.querySelector('.id-wrapper');
  const $passWrapper = document.querySelector('.pass-wrapper');
  const $passCheck = document.querySelector('.pass-check');
  const $inputId = document.querySelector('input[name=account]');
  const $inputPw = document.querySelector('input[name=password]');
  const $prevBtn = document.getElementById('prev-btn');
  const $h2Id = document.querySelector('.id-wrapper h2');

  $inputId.addEventListener('keyup', () => {
  });

  $idBtn.addEventListener('click', async (e) => {
    e.preventDefault();
    // 이메일 형식
    const emailRegExp = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    if (!emailRegExp.test($inputId.value)) {
      $h2Id.textContent = '이메일 형식이 올바르지 않습니다.';
      $h2Id.style.color = 'red';
      return;
    }

    const res = await fetch(
        `http://localhost:8083/store/check?type=account&keyword=\${$inputId.value}`);
    const result = await res.json();
    if (result) {
      $h2Id.textContent = '이미 사용중인 이메일입니다.';
      $h2Id.style.color = 'red';
      return;
    }
    $idWrapper.classList.add('none');
    $passWrapper.classList.remove('none');
  });

  $inputPw.addEventListener('keyup', (e) => {
    $passCheck.classList.remove('none');
  });

  $passBtn.addEventListener('click', (e) => {
    e.preventDefault();
    console.log($inputId.value, $inputPw.value);
    document.querySelector('form').submit();
  });

  $prevBtn.addEventListener('click', (e) => {
    e.preventDefault();
    $idWrapper.classList.remove('none');
    $passWrapper.classList.add('none');
  });
</script>

  <!-- 공통푸터 -->
  <%@ include file="../include/footer.jsp" %>
</body>
</html>
